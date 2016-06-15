use rustc_serialize::json;
use std::collections::BTreeMap;
use mysql::{FromRow, Row, from_row};
use mysql::Result as MySqlResult;
use mysql::error::Error as MySqlError;
use std::num::ParseFloatError;
use std::str::FromStr;
use std::string::ParseError;
use regex::Regex;
use std::convert::From;

#[derive(Debug)]
pub enum BeerModelError {
    CoordinateFloatParse(ParseFloatError),
    CoordinateStringParse(String)
}

#[derive(RustcEncodable)]
pub struct Beer {
    pub id: u32,
    pub name: String,
    pub brewery: String,
    pub beer_type: String,
    pub description: String
}

impl FromRow for Beer {
    fn from_row(row: Row) -> Beer {
        FromRow::from_row_opt(row).ok().expect("Could not convert to type Beer")
    }

    fn from_row_opt(row: Row) -> MySqlResult<Beer> {

        let (id, name, t, brewery, desc) = from_row(row);

        Ok(Beer {
            id: id,
            name: name,
            brewery: brewery,
            beer_type: t,
            description: desc
        })
    }
}

#[derive(RustcEncodable,Copy,Clone)]
pub struct Point {
    pub lat: f64,
    pub lon: f64
}

impl From<Point> for String {
    fn from(p: Point) -> Self {
        format!("POINT({} {})", p.lat, p.lon)
    }
}

impl Point {
    pub fn from_strings(lat: &str, lon: &str) -> Result<Point, BeerModelError> {
        let latitude = match lat.parse::<f64>() {
            Ok(f) => f,
            Err(s) => return Err(BeerModelError::CoordinateFloatParse(s))
        };
        let longitude = match lon.parse::<f64>() {
            Ok(f) => f,
            Err(s) => return Err(BeerModelError::CoordinateFloatParse(s))
        };
        Ok(Point {
            lat: latitude,
            lon: longitude
        })
    }
}

impl FromStr for Point {
    type Err = BeerModelError;
    fn from_str(s: &str) -> Result<Self, <Self as FromStr>::Err> {
        lazy_static! {
            static ref POINTREGEX: Regex = Regex::new(r"^POINT\((?P<lat>\d+[.]\d+) (?P<lon>\d+[.]\d+)\)$").unwrap();
        }
        println!("{}", s);
        let matches = match POINTREGEX.captures(s) {
            Some(m) => m,
            None => return Err(BeerModelError::CoordinateStringParse(String::from("Invalid POINT format")))
        };
        if let (Some(lat), Some(lon)) = (matches.name("lat"), matches.name("lon")) {
            return Point::from_strings(lat, lon);
        }
        Err(BeerModelError::CoordinateStringParse(String::from("Unable to parse coordinates with unknown reason")))
    }
}

#[derive(RustcEncodable)]
pub struct Pub {
    pub id: u32,
    pub name: String,
    pub description: String,
    pub serves: Vec<Beer>,
    pub distance: Option<u64>,
    pub location: Point
}

impl Pub {
    pub fn new(id: u32, n: String, d: String, loc: Point, dist: Option<u64>, serving: Option<Vec<Beer>>) -> Pub {
        Pub {
            id: id,
            serves: match serving {
                Some(b) => b,
                None => vec![]
            },
            name: n.to_string(),
            description: d.to_string(),
            location: loc,
            distance: dist
        }
    }
}

impl FromRow for Pub {
    fn from_row(row: Row) -> Pub {
        FromRow::from_row_opt(row).ok().expect("Could not convert to type Beer")
    }

    fn from_row_opt(row: Row) -> MySqlResult<Pub> {
        let (id, name, description, gps): (u32, String, String, String) = from_row(row);
        let coords = gps.parse::<Point>().unwrap();
        Ok(
            Pub::new(
                id,
                name,
                description,
                coords,
                None,
                None
            )
        )
    }
}
