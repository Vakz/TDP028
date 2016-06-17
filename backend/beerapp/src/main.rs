#![feature(custom_derive)]
#[macro_use] extern crate mysql;
extern crate rustc_serialize;
extern crate iron;
#[macro_use] extern crate router;
extern crate urlencoded;
extern crate crossbeam;
extern crate regex;
#[macro_use] extern crate lazy_static;

use mysql::error::Error as MySqlError;
use iron::mime::Mime;
use iron::prelude::*;
use iron::status;
use router::Router;
use mysql::{Pool, Row, from_row};
use urlencoded::UrlEncodedQuery;
use rustc_serialize::json::{self};
use std::collections::HashMap;
use iron::middleware::Handler;
use mysql::Result as MySqlResult;

use std::sync::Arc;

mod model;

use model::*;

fn json_response(body: String) -> IronResult<Response> {
    let content_type = "application/json; charset=utf-8".parse::<Mime>().unwrap();
    Ok(Response::with((content_type, status::Ok, body)))
}

fn build_pool() -> mysql::Pool {
    let mut opts = mysql::OptsBuilder::new();
    opts.ip_or_hostname(Some("127.0.0.1"))
    .db_name(Some("beerapp"))
    .user(Some("beerapp"));

    mysql::Pool::new(opts).unwrap()
}

fn get_single<T, F>(res: mysql::Result<mysql::QueryResult>, conv: F) -> Option<T>
where F: Fn(Row) -> T {
    match res.unwrap().next() {
        Some(o) => Some(conv(o.unwrap())),
        None => None
    }
}

fn get_all<T, F>(res: mysql::Result<mysql::QueryResult>, conv: F) -> Option<Vec<T>>
where F: Fn(Row) -> T {
    let r = res.map(|result|
        result.map(|x| x.unwrap()).map(|row| {
            conv(row)
        }).collect::<Vec<T>>()
    ).unwrap();
    if r.len() > 0 { Some(r) } else { None }
}

fn get_parameters(params: Vec<String>, query: &HashMap<String, Vec<String>>) -> Option<HashMap<String, String>> {
    let mut res: HashMap<String, String> = HashMap::new();
    for param in &params {
        match query.get(param) {
            Some(v) => match v.get(0) {
                Some(q) if q.len() > 0 => res.insert(param.to_string(), q.clone()),
                Some(_) => return None,
                None => return None
            },
            None => return None
        };
    };
    return Some(res);
}

struct Search {
    db: Pool,
}

impl Handler for Search {
    fn handle(&self, req: &mut Request) -> IronResult<Response> {
        if let Ok(r) = req.get_ref::<UrlEncodedQuery>() {
            if let Some(query) = get_parameters(vec![String::from("query")], r) {
                let f = |row| {
                    let (id, name, t, brewery, desc) = from_row(row);

                    Beer {
                        id: id,
                        name: name,
                        brewery: brewery,
                        beer_type: t,
                        description: desc
                    }
                };

                let q = query.get("query").unwrap();
                let b: Vec<Beer> = get_all(self.db.prep_exec(r"CALL search(:query);", params!{"query" => q}), f).unwrap();
                return json_response(json::encode(&b).unwrap());
            } else {
                return Ok(Response::with((status::ServiceUnavailable, String::from("Unknown database error"))));
            }
        }
        Ok(Response::with((status::UnprocessableEntity, String::from("Missing searchword"))))
    }
}

struct GetMenu {
    db: Pool,
}

impl Handler for GetMenu {
    fn handle(&self, req: &mut Request) -> IronResult<Response> {
        if let Ok(r) = req.get_ref::<UrlEncodedQuery>() {
            if let Some(query) = get_parameters(vec![String::from("id")], r) {
                let id = query.get("id").unwrap();
                match id.parse::<u32>() {
                    Ok(_) => {},
                    Err(_) => return Ok(Response::with((status::UnprocessableEntity, String::from("Invalid id"))))
                };

                let f = |row| {
                    let (id, name, t, brewery, desc) = from_row(row);

                    Beer {
                        id: id,
                        name: name,
                        brewery: brewery,
                        beer_type: t,
                        description: desc
                    }
                };

                if let Some(menu) = get_all::<Beer, _>(self.db.prep_exec(r"CALL menu(:id);", params!{"id" => id}), f) {
                    return json_response(json::encode(&menu).unwrap());
                } else {
                    return Ok(Response::with((status::UnprocessableEntity, String::from("No pub with that id"))));
                }

            }
        }
        Ok(Response::with((status::UnprocessableEntity, String::from("Missing id"))))
    }
}

struct GetPub {
    db: Pool,
}

impl Handler for GetPub {
    fn handle(&self, req: &mut Request) -> IronResult<Response> {
        if let Ok(r) = req.get_ref::<UrlEncodedQuery>() {
            if let Some(query) = get_parameters(vec![String::from("id")], r) {
                let id = query.get("id").unwrap();
                match id.parse::<u32>() {
                    Ok(_) => {}
                    Err(_) => return Ok(Response::with((status::UnprocessableEntity, String::from("Invalid id"))))
                };

                let f = |row| {
                    let (id, name, description, gps): (u32, String, String, String) = from_row(row);
                    let coords = gps.parse::<Point>().unwrap();
                    Pub::new(
                        id,
                        name,
                        description,
                        coords,
                        None,
                        None
                    )
                };

                if let Some(mut p) = get_single::<Pub, _>(self.db.prep_exec(r"SELECT * FROM PubText WHERE id = :id", params!{"id" => id}), f) {
                    let f = |row| {
                        let (id, name, t, brewery, desc) = from_row(row);

                        Beer {
                            id: id,
                            name: name,
                            brewery: brewery,
                            beer_type: t,
                            description: desc
                        }
                    };

                    if let Some(menu) = get_all::<Beer, _>(self.db.prep_exec(r"CALL menu(:id);", params!{"id" => id}), f) {
                        p.serves = menu;
                        return json_response(json::encode(&p).unwrap());
                    }

                }  else {
                    return Ok(Response::with((status::UnprocessableEntity, String::from("No pub with that id"))));
                }
            }
        }
        Ok(Response::with((status::UnprocessableEntity, String::from("Missing id"))))
    }
}

struct GetNearbyPubs {
    db: Pool,
}

impl Handler for GetNearbyPubs {
    fn handle(&self, req: &mut Request) -> IronResult<Response> {
        if let Ok(r) = req.get_ref::<UrlEncodedQuery>() {
            if let Some(query) = get_parameters(vec![String::from("lat"), String::from("lon"), String::from("distance")], r) {
                let (lat, lon, distance) = (query.get("lat").unwrap(), query.get("lon").unwrap(), query.get("distance").unwrap());
                let loc = match Point::from_strings(lat, lon) {
                    Ok(l) => l,
                    Err(_) => return Ok(Response::with((status::UnprocessableEntity, String::from("Invalid location"))))
                };

                if let Err(_) = distance.parse::<u32>() {
                    return Ok(Response::with((status::UnprocessableEntity, String::from("Invalid distance"))));
                }

                let f = |row| {
                    let (id, name, description, gps, distance): (u32, String, String, String, u64) = from_row(row);
                    let coords = gps.parse::<Point>().unwrap();
                    Pub::new(
                        id,
                        name,
                        description,
                        coords,
                        Some(distance),
                        None
                    )
                };
                if let Some(pubs) = get_all::<Pub, _>(self.db.prep_exec("CALL findWithin(GeomFromText(:point), :distance)",
                    params!{"point" => String::from(loc), "distance" => distance.clone() }), f) {
                    return json_response(json::encode(&pubs).unwrap());
                } else {
                    return Ok(Response::with((status::UnprocessableEntity, String::from("No pubs within this distance"))));
                }
            }
        }
        Ok(Response::with((status::UnprocessableEntity, String::from("Missing parameters"))))
    }
}

struct GetBeer {
    db: Pool,
}

impl Handler for GetBeer {
    fn handle(&self, req: &mut Request) -> IronResult<Response> {
        if let Ok(r) = req.get_ref::<UrlEncodedQuery>() {
            if let Some(query) = get_parameters(vec![String::from("id")], r) {
                let id = query.get("id").unwrap();
                match id.parse::<u32>() {
                    Ok(_) => {}
                    Err(_) => return Ok(Response::with((status::UnprocessableEntity, String::from("Invalid id"))))
                };

                let f = |row| {
                    let (id, name, t, brewery, desc) = from_row(row);

                    Beer {
                        id: id,
                        name: name,
                        brewery: brewery,
                        beer_type: t,
                        description: desc
                    }
                };

                if let Some(p) = get_single::<Beer, _>(self.db.prep_exec(r"SELECT * FROM Beer WHERE id = :id", params!{"id" => id}), f) {
                    return json_response(json::encode(&p).unwrap());
                }  else {
                    return Ok(Response::with((status::UnprocessableEntity, String::from("No beer with that id"))));
                }
            }
        }
        Ok(Response::with((status::UnprocessableEntity, String::from("Missing id"))))
    }
}

struct ServingWithin {
    db: Pool,
}

impl Handler for ServingWithin {
    fn handle(&self, req: &mut Request) -> IronResult<Response> {
        if let Ok(r) = req.get_ref::<UrlEncodedQuery>() {
            if let Some(query) = get_parameters(vec![String::from("id"), String::from("lat"), String::from("lon"), String::from("distance")], r) {
                let (lat, lon, distance, id) = (query.get("lat").unwrap(), query.get("lon").unwrap(), query.get("distance").unwrap(), query.get("id").unwrap());
                let loc = match Point::from_strings(lat, lon) {
                    Ok(l) => l,
                    Err(_) => return Ok(Response::with((status::UnprocessableEntity, String::from("Invalid location"))))
                };

                if let Err(_) = distance.parse::<u32>() {
                    return Ok(Response::with((status::UnprocessableEntity, String::from("Invalid distance"))));
                }

                if let Err(_) = id.parse::<u32>() {
                    return Ok(Response::with((status::UnprocessableEntity, String::from("Invalid id"))));
                }

                let f = |row| {
                    let (id, name, description, gps, distance): (u32, String, String, String, u64) = from_row(row);
                    let coords = gps.parse::<Point>().unwrap();
                    Pub::new(
                        id,
                        name,
                        description,
                        coords,
                        Some(distance),
                        None
                    )
                };

                if let Some(p) = get_all::<Pub, _>(self.db.prep_exec(r"CALL servingWithin(GeomFromText(:point), :distance, :id)",
                params!{"id" => id, "point" => &String::from(loc), "distance" => &distance}), f) {
                    return json_response(json::encode(&p).unwrap());
                } else {
                    return Ok(Response::with((status::UnprocessableEntity, String::from("No pubs serving this beer within that distance"))));
                }

            }
        }
        Ok(Response::with((status::UnprocessableEntity, String::from("Missing parameters"))))
    }
}

struct GetClosest {
    db: Pool,
}

impl Handler for GetClosest {
    fn handle(&self, req: &mut Request) -> IronResult<Response> {
        if let Ok(r) = req.get_ref::<UrlEncodedQuery>() {
            if let Some(query) = get_parameters(vec![String::from("lat"), String::from("lon")], r) {
                let (lat, lon) = (query.get("lat").unwrap(), query.get("lon").unwrap());
                let loc = match Point::from_strings(lat, lon) {
                    Ok(l) => l,
                    Err(_) => return Ok(Response::with((status::UnprocessableEntity, String::from("Invalid location"))))
                };

                let f = |row| {
                    let (id, name, description, gps, distance): (u32, String, String, String, u64) = from_row(row);
                    let coords = gps.parse::<Point>().unwrap();
                    Pub::new(
                        id,
                        name,
                        description,
                        coords,
                        Some(distance),
                        None
                    )
                };

                if let Some(mut p) = get_single::<Pub, _>(self.db.prep_exec(r"CALL findClosest(GeomFromText(:point))", params!{"point" => String::from(loc)}), f) {
                    let f = |row| {
                        let (id, name, t, brewery, desc) = from_row(row);

                        Beer {
                            id: id,
                            name: name,
                            brewery: brewery,
                            beer_type: t,
                            description: desc
                        }
                    };

                    if let Some(menu) = get_all::<Beer, _>(self.db.prep_exec(r"CALL menu(:id);", params!{"id" => p.id}), f) {
                        p.serves = menu;
                        return json_response(json::encode(&p).unwrap());
                    } else {
                        return Ok(Response::with((status::ServiceUnavailable, String::from("Unknown database error"))));
                    }
                } else {
                    return Ok(Response::with((status::ServiceUnavailable, String::from("Unknown database error"))));
                }
            }
        }
        Ok(Response::with((status::UnprocessableEntity, String::from("Missing parameters"))))
    }
}

fn main() {
    let pool = build_pool();

    Iron::new(router!(
        get "/search" => Search { db: pool.clone() },
        get "/menu" => GetMenu { db: pool.clone() },
        get "/pub" => GetPub { db: pool.clone() },
        get "/beer" => GetBeer {db: pool.clone() },
        get "/getNearby" => GetNearbyPubs { db: pool.clone() },
        get "/pubsServing" => ServingWithin { db: pool.clone() },
        get "/getClosest" => GetClosest { db: pool.clone() }
    )).http("localhost:8888").unwrap();
}
