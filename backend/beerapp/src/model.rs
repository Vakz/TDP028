mod model {
    use rustc_serialize::json::{self, ToJson, Json};
    use std::collections::BTreeMap;

    pub struct Beer {
        name: String,
        brewery: String,
        beer_type: String,
        description: String
    }

    impl ToJson for Beer {
        fn to_json(&self) -> Json {
            let mut t = BTreeMap::new();
            t.insert("name".to_string(),  self.name.to_json());
            t.insert("brewery".to_string(),  self.brewery.to_json());
            t.insert("beer_type".to_string(),  self.beer_type.to_json());
            t.insert("description".to_string(),  self.description.to_json());
            Json::Object(t)
        }
    }

    pub struct Point {
        lat: f64,
        lon: f64
    }

    impl ToJson for Point {
        fn to_json(&self) -> Json {
            let mut t = BTreeMap::new();
            t.insert("lat".to_string(), self.lat.to_json());
            t.insert("lon".to_string(), self.lat.to_json());
            Json::Object(t)
        }
    }

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

    impl ToJson for Pub {
        fn to_json(&self) -> Json {
            let mut t = BTreeMap::new();
            t.insert("name".to_string(), self.name.to_json());
            t.insert("description".to_string(), self.description.to_json());
            t.insert("location".to_string(), self.location.to_json());
            t.insert("serves".to_string(), self.serves.to_json());
            Json::Object(t)
        }
    }
}
