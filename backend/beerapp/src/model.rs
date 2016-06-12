use rustc_serialize::json;
use std::collections::BTreeMap;
use mysql::{FromRow, Row};
use mysql::Result as MySqlResult;
use mysql::error::Error as MySqlError;
use std::num::ParseFloatError;

pub enum BeerModelError {
    CoordinateParse(ParseFloatError)
}

#[derive(RustcEncodable)]
pub struct Beer {
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
        Ok(Beer {
            name: row["name"].into_str(),
            brewery: row["brewery"].into_str(),
            beer_type: row["type"].into_str(),
            description: row["description"].into_str()
        })
    }
}
/*
impl ToJson for Beer {
    fn to_json(&self) -> Json {
        let mut t = BTreeMap::new();
        t.insert("name".to_string(),  self.name.to_json());
        t.insert("brewery".to_string(),  self.brewery.to_json());
        t.insert("beer_type".to_string(),  self.beer_type.to_json());
        t.insert("description".to_string(),  self.description.to_json());
        Json::Object(t)
    }
}*/

pub struct Point {
    lat: f64,
    lon: f64
}

impl Point {
    fn from_str(lat: &str, lon: &str) -> Result<Point, BeerModelError> {
        let latitude = match lat.parse::<f64>() {
            Ok(f) => f,
            Err(s) => return Err(BeerModelError::CoordinateParse(s))
        };
        let longitude = match lon.parse::<f64>() {
            Ok(f) => f,
            Err(s) => return Err(BeerModelError::CoordinateParse(s))
        };
        Ok(Point {
            lat: latitude,
            lon: longitude
        })
    }
}

/*
impl ToJson for Point {
    fn to_json(&self) -> Json {
        let mut t = BTreeMap::new();
        t.insert("lat".to_string(), self.lat.to_json());
        t.insert("lon".to_string(), self.lat.to_json());
        Json::Object(t)
    }
}*/

pub struct Pub {
    name: String,
    description: String,
    serves: Vec<Beer>,
    location: Point
}

impl Pub {
    pub fn new(n: &str, d: &str, loc: Point, serving: Option<Vec<Beer>>) -> Pub {
        Pub {
            serves: match serving {
                Some(b) => b,
                None => vec![]
            },
            name: n.to_string(),
            description: d.to_string(),
            location: loc
        }
    }
}
/*
impl ToJson for Pub {
    fn to_json(&self) -> Json {
        let mut t = BTreeMap::new();
        t.insert("name".to_string(), self.name.to_json());
        t.insert("description".to_string(), self.description.to_json());
        t.insert("location".to_string(), self.location.to_json());
        t.insert("serves".to_string(), self.serves.to_json());
        Json::Object(t)
    }
}*/
