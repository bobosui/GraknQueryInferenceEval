USING PERIODIC COMMIT 500
LOAD CSV WITH HEADERS FROM 'file:///grid-entities-neo.csv' AS line
CREATE (:Entity { name: line.name})

USING PERIODIC COMMIT 500
LOAD CSV WITH HEADERS FROM 'file:///grid-horizontal-neo.csv' AS line
MERGE (entity1:Entity { name: line.name1 })
MERGE (entity2:Entity { name: line.name2 })
CREATE (entity1)-[:HORIZONTAL]->(entity2)

USING PERIODIC COMMIT 500
LOAD CSV WITH HEADERS FROM 'file:///grid-vertical-neo.csv' AS line
MERGE (entity1:Entity { name: line.name1 })
MERGE (entity2:Entity { name: line.name2 })
CREATE (entity1)-[:VERTICAL]->(entity2)