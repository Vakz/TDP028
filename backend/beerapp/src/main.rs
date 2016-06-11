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

fn search(db: &mut Pool, req: &mut Request) -> IronResult<Response> {
    let params = req.get_ref::<UrlEncodedQuery>().unwrap();
    let query = match params.get("query") {
        Some(q) => q.get(0).unwrap(),
        None => return Ok(Response::with((status::UnprocessableEntity, String::from("Missing searchword"))))
    };

    println!("Got search request with keyword \"{}\"", query);
    let names: Vec<Beer> = db.prep_exec(r"CALL search(:query);", (params!{"query" => query})).
    map(|result|
        result.map(|x| x.unwrap()).map(|row| {
            mysql::from_row(row)
        }).collect()
    ).unwrap();
    println!("{}", names.get(0).unwrap().description);
    json_response(json::encode(&names[0]).unwrap())
}

fn main() {
    let pool = build_pool();
    let mut router = Router::new();
    router.get("/search", move |req: &mut Request| {
        let mut db = pool.clone();
        search(&mut db, req)
    });

    Iron::new(router).http("localhost:8888").unwrap();
    /*

    for n in names {
        println!("{}", n);
    }*/
}
