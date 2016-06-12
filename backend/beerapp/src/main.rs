#![feature(custom_derive)]
#[macro_use] extern crate mysql;
extern crate rustc_serialize;
extern crate iron;
extern crate router;
extern crate urlencoded;

use iron::mime::Mime;
use iron::prelude::*;
use iron::status;
use router::Router;
use mysql::Pool;
use urlencoded::UrlEncodedQuery;
use rustc_serialize::json::{self};
use std::collections::HashMap;

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

fn get_single<T: mysql::FromRow>(res: mysql::Result<mysql::QueryResult>) -> Option<T> {
    match res.unwrap().next() {
        Some(o) => Some(mysql::from_row::<T>(o.unwrap())),
        None => None
    }
}

fn get_all<T: mysql::FromRow>(res: mysql::Result<mysql::QueryResult>) -> Option<Vec<T>> {
    Some(res.map(|result|
        result.map(|x| x.unwrap()).map(|row| {
            mysql::from_row(row)
        }).collect::<Vec<T>>()
    ).unwrap())
}

fn get_parameters(params: Vec<String>, query: &HashMap<String, Vec<String>>) -> Option<HashMap<String, String>> {
    let mut res: HashMap<String, String> = HashMap::new();
    for param in &params {
        match query.get(param) {
            Some(v) => match v.get(0) {
                Some(q) => res.insert(param.to_string(), q.clone()),
                None => return None
            },
            None => return None
        };
    };
    return Some(res);
}

fn search(db: &mut Pool, req: &mut Request) -> IronResult<Response> {
    if let Some(query) = get_parameters(vec![String::from("query")], req.get_ref::<UrlEncodedQuery>().unwrap()) {
        let q = query.get("query").unwrap();
        let b: Vec<Beer> = get_all(db.prep_exec(r"CALL search(:query);", params!{"query" => q})).unwrap();
        json_response(json::encode(&b).unwrap())
    } else {
        Ok(Response::with((status::UnprocessableEntity, String::from("Missing searchword"))))
    }
}

fn get_menu(db: &mut Pool, req: &mut Request) -> IronResult<Response> {
    if let Some(query) = get_parameters(vec![String::from("id")], req.get_ref::<UrlEncodedQuery>().unwrap()) {
        let id = query.get("id").unwrap();
        match id.parse::<i32>() {
            Ok(_) => {},
            Err(_) => return Ok(Response::with((status::UnprocessableEntity, String::from("Invalid id"))))
        };
        let menu: Vec<Beer> = get_all(db.prep_exec(r"CALL menu(:id);", params!{"id" => id})).unwrap();
        json_response(json::encode(&menu).unwrap())
    } else {
        Ok(Response::with((status::UnprocessableEntity, String::from("Missing id"))))
    }
}

fn main() {
    let pool = build_pool();
    let mut router = Router::new();
    router.get("/search", |req: &mut Request| {
        let mut db = pool.clone();
        search(&mut db, req)
    });
    router.get("/menu", |req: &mut Request| {
        let mut db = pool.clone();
        get_menu(&mut db, req)
    });

    Iron::new(router).http("localhost:8888").unwrap();
}
