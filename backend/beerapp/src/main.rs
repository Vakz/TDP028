#[macro_use] extern crate mysql;
extern crate rustc_serialize;

mod model;

fn build_pool() -> mysql::Pool {
    let mut opts = mysql::OptsBuilder::new();
    opts.ip_or_hostname(Some("127.0.0.1"))
    .db_name(Some("beerapp"))
    .user(Some("beerapp"));

    mysql::Pool::new(opts).unwrap()
}

fn main() {
    let pool = build_pool();


    let names: Vec<String> = pool.prep_exec(r"CALL selectAll();", ()).
    map(|result|
        result.map(|x| x.unwrap()).map(|row| {
            mysql::from_row(row)
        }).collect()
    ).unwrap();
    for n in names {
        println!("{}", n);
    }
}
