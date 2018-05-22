MATCH (n) DETACH DELETE n;

CREATE (id:GlobalUniqueId { count:1000 });
CREATE (n:User:Active:PERSISTENT { id:1 }) -[:Currently]-> (d:Data { created:TIMESTAMP(), name:"Adam", password:"Hemligt" });
CREATE (n:User:Active:PERSISTENT { id:2 }) -[:Currently]-> (d:Data { created:TIMESTAMP(), name:"Bertil", password:"Hemligt" });
MATCH (a:User) WHERE a.id = 1 CREATE (a)-[:Is]->(n:Player:Active:PERSISTENT { id:10 }) -[:Currently]-> (d:Data { created:TIMESTAMP(), name:"Ceasar" });
MATCH (a:Player) WHERE a.id = 10 CREATE (a)-[:Possess]->(n:Deck:Active:PERSISTENT { id:20 })-[:Currently]->(d:Data {created:TIMESTAMP(), name:"Deck 1", format:"STANDARD", theme:"Testing", black:1, white:1, red:0, green:0, blue:0, colorless:0});
MATCH (a:Player) WHERE a.id = 10 CREATE (a)-[:Possess]->(n:Deck:Active:PERSISTENT { id:21 })-[:Currently]->(d:Data {created:TIMESTAMP(), name:"Deck 2", format:"PAUPER", theme:"Testing", black:1, white:1, red:1, green:1, blue:1, colorless:1});
MATCH (a:Player) WHERE a.id = 10 CREATE (a)-[:Possess]->(n:Deck:Active:PERSISTENT { id:22 })-[:Currently]->(d:Data {created:TIMESTAMP(), name:"Deck 3", format:"BOOSTER_HANDOVER", theme:"Testing", black:0, white:0, red:0, green:0, blue:0, colorless:0});

