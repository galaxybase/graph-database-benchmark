schema.propertyKey("ID").asText().ifNotExist().create();

schema.vertexLabel("ve").properties("ID").primaryKeys("ID").ifNotExist().create();
schema.edgeLabel("ed").sourceLabel("ve").targetLabel("ve").ifNotExist().create();