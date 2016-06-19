#![feature(custom_derive)]
#[macro_use] extern crate mysql;
extern crate rustc_serialize;
extern crate iron;
#[macro_use] extern crate router;
extern crate urlencoded;
extern crate regex;
#[macro_use] extern crate lazy_static;

use iron::mime::Mime;
use iron::prelude::*;
use iron::status;
use mysql::{Pool, Row, from_row};
use urlencoded::UrlEncodedQuery;
use rustc_serialize::json::{self};
use std::collections::HashMap;
use iron::middleware::Handler;

mod model;

use model::*;

struct ModelConverter;

impl ModelConverter {
    fn beerrow(row: Row) -> Beer {
        let (id, name, t, brewery, desc) = from_row(row);

        Beer {
            id: id,
            name: name,
            brewery: brewery,
            beer_type: t,
            description: desc
        }
    }

    fn pubrow(row: Row) -> Pub {
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
    }

    fn distancerow(row: Row) -> Pub {
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
    }
}

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

fn get_parameters(params: &[&'static str], query: &HashMap<String, Vec<String>>) -> Option<HashMap<&'static str, String>> {
    let mut res: HashMap<&'static str, String> = HashMap::new();
    for param in params {
        let s: String = param.to_string();
        match query.get(&s) {
            Some(v) => match v.get(0) {
                Some(q) if q.len() > 0 => res.insert(param, q.clone()),
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
            if let Some(query) = get_parameters(&["query"], r) {

                let q = query.get("query").unwrap();
                let b: Vec<Beer> = get_all(self.db.prep_exec(r"CALL search(:query);", params!{"query" => q}), ModelConverter::beerrow).unwrap_or(Vec::new());
                return json_response(json::encode(&b).unwrap());
            }
            return Ok(Response::with((status::ServiceUnavailable, String::from("Unknown database error"))));
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
            if let Some(query) = get_parameters(&["id"], r) {
                let id = query.get("id").unwrap();
                match id.parse::<u32>() {
                    Ok(_) => {},
                    Err(_) => return Ok(Response::with((status::UnprocessableEntity, String::from("Invalid id"))))
                };

                if let Some(menu) = get_all::<Beer, _>(self.db.prep_exec(r"CALL menu(:id);", params!{"id" => id}), ModelConverter::beerrow) {
                    return json_response(json::encode(&menu).unwrap());
                }
                return Ok(Response::with((status::UnprocessableEntity, String::from("No pub with that id"))));
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
            if let Some(query) = get_parameters(&["id"], r) {
                let id = query.get("id").unwrap();
                match id.parse::<u32>() {
                    Ok(_) => {}
                    Err(_) => return Ok(Response::with((status::UnprocessableEntity, String::from("Invalid id"))))
                };

                if let Some(mut p) = get_single::<Pub, _>(self.db.prep_exec(r"SELECT * FROM PubText WHERE id = :id", params!{"id" => id}), ModelConverter::pubrow) {
                    if let Some(menu) = get_all::<Beer, _>(self.db.prep_exec(r"CALL menu(:id);", params!{"id" => id}), ModelConverter::beerrow) {
                        p.serves = menu;
                        return json_response(json::encode(&p).unwrap());
                    }
                }
                return Ok(Response::with((status::UnprocessableEntity, String::from("No pub with that id"))));
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
            if let Some(query) = get_parameters(&["lat", "lon", "distance"], r) {
                let (lat, lon, distance) = (query.get("lat").unwrap(), query.get("lon").unwrap(), query.get("distance").unwrap());
                let loc = match Point::from_strings(lat, lon) {
                    Ok(l) => l,
                    Err(_) => return Ok(Response::with((status::UnprocessableEntity, String::from("Invalid location"))))
                };

                if let Err(_) = distance.parse::<u32>() {
                    return Ok(Response::with((status::UnprocessableEntity, String::from("Invalid distance"))));
                }

                if let Some(pubs) = get_all::<Pub, _>(self.db.prep_exec("CALL findWithin(GeomFromText(:point), :distance)",
                    params!{"point" => String::from(loc), "distance" => distance.clone() }), ModelConverter::distancerow) {
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
            if let Some(query) = get_parameters(&["id"], r) {
                let id = query.get("id").unwrap();
                match id.parse::<u32>() {
                    Ok(_) => {}
                    Err(_) => return Ok(Response::with((status::UnprocessableEntity, String::from("Invalid id"))))
                };

                if let Some(p) = get_single::<Beer, _>(self.db.prep_exec(r"SELECT * FROM Beer WHERE id = :id", params!{"id" => id}), ModelConverter::beerrow) {
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
            if let Some(query) = get_parameters(&["lat", "lon", "distance", "id"], r) {
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

                if let Some(p) = get_all::<Pub, _>(self.db.prep_exec(r"CALL servingWithin(GeomFromText(:point), :distance, :id)",
                params!{"id" => id, "point" => &String::from(loc), "distance" => &distance}), ModelConverter::distancerow) {
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
            if let Some(query) = get_parameters(&["lat", "lon"], r) {
                let (lat, lon) = (query.get("lat").unwrap(), query.get("lon").unwrap());
                let loc = match Point::from_strings(lat, lon) {
                    Ok(l) => l,
                    Err(_) => return Ok(Response::with((status::UnprocessableEntity, String::from("Invalid location"))))
                };

                if let Some(mut p) = get_single::<Pub, _>(self.db.prep_exec(r"CALL findClosest(GeomFromText(:point))", params!{"point" => String::from(loc)}), ModelConverter::distancerow) {

                    if let Some(menu) = get_all::<Beer, _>(self.db.prep_exec(r"CALL menu(:id);", params!{"id" => p.id}), ModelConverter::beerrow) {
                        p.serves = menu;
                        return json_response(json::encode(&p).unwrap());
                    }
                }
                return Ok(Response::with((status::ServiceUnavailable, String::from("Unknown database error"))));
            }
        }
        Ok(Response::with((status::UnprocessableEntity, String::from("Missing parameters"))))
    }
}

struct Suggestion {
    db: Pool,
}

impl Handler for Suggestion {
    fn handle(&self, req: &mut Request) -> IronResult<Response> {
        if let Ok(r) = req.get_ref::<UrlEncodedQuery>() {
            if let Some(query) = get_parameters(&["lat", "lon", "filter"], r) {
                let (lat, lon) = (query.get("lat").unwrap(), query.get("lon").unwrap());
                let loc = match Point::from_strings(lat, lon) {
                    Ok(l) => l,
                    Err(_) => return Ok(Response::with((status::UnprocessableEntity, String::from("Invalid location"))))
                };

                let filter: &String = query.get("filter").unwrap();
                match json::decode::<Vec<u32>>(filter) {
                    Ok(_) => {},
                    Err(_) => return Ok(Response::with((status::UnprocessableEntity, String::from("Invalid format of filter"))))
                };

                if let Some(b) = get_single::<Beer, _>(self.db.prep_exec(r"CALL suggestion(GeomFromText(:point), :filter)",
                params!{"point" => String::from(loc), "filter" => filter.clone()}), ModelConverter::beerrow) {
                    return json_response(json::encode(&b).unwrap());
                }
                return Ok(Response::with((status::UnprocessableEntity, String::from("Found no suggestions"))));
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
        get "/getClosest" => GetClosest { db: pool.clone() },
        get "/suggestion" => Suggestion { db: pool.clone() }
    )).http("localhost:8888").unwrap();
}
