MATCH (n: ACOMPANY_Service_Client {name:"svc-user-client", org:"a.company"}) OPTIONAL MATCH (n)-[r]-() DELETE r;
MATCH a WHERE NOT (a)-[:Depends]-() DELETE a;
MERGE (a: ACOMPANY_Service_Client {name:"svc-user-client", org:"a.company"}) ON MATCH SET a.crossCompiled="2.9.2, 2.10.2", a.lastVersion="1.0" ON CREATE SET a.crossCompiled="2.9.2, 2.10.2", a.lastVersion="1.0";
MERGE (a: External {name:"c3p0", org:"c3p0"});
MERGE (a: External {name:"logback-classic", org:"ch.qos.logback"});
MERGE (a: External {name:"scala-library", org:"org.scala-lang"});
MATCH (a {name:"c3p0", org:"c3p0"}), (b {name:"svc-user-client", org:"a.company"}) CREATE UNIQUE (b)-[r:Depends {version:"0.9.1.2", declaration:"c3p0:c3p0:0.9.1.2"}]->(a);
MATCH (a {name:"logback-classic", org:"ch.qos.logback"}), (b {name:"svc-user-client", org:"a.company"}) CREATE UNIQUE (b)-[r:Depends {version:"1.0.6", declaration:"ch.qos.logback:logback-classic:1.0.6"}]->(a);
MATCH (a {name:"scala-library", org:"org.scala-lang"}), (b {name:"svc-user-client", org:"a.company"}) CREATE UNIQUE (b)-[r:Depends {version:"2.10.3", declaration:"org.scala-lang:scala-library:2.10.3"}]->(a);