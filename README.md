# mbrainz

Java maven project for performing queries and lookups on MusicBrainz database using the MusicBrainz XML Web Service API.

[MusicBrainz API documentation](https://musicbrainz.org/doc/Development/XML_Web_Service/Version_2)
  
### Dependencies  
Apaches Lucene library (see pom.xml)

### Examples

Looking up an artist with MusicBrainz id.
```java
MBrainzLookup lookup = new MBrainzLookup(
    "application name", 
    "version number", 
    "contact information");
String res = lookup.doRequest(
    Entity.ARTIST, 
    "070d193a-845c-479f-980e-bef15710653e");
System.out.println(res); // Prints out artist information
```

Querying an artist with Lucene syntax.
```java
MBrainzQuery query = new MBrainzQuery(
    "application name", 
    "version number", 
    "contact information");
String res = query.doRequest(
    Entity.ARTIST, 
    "name:prince AND country:US");
System.out.println(res) // Prints out information of all artists 
                        // from United States whose name contains
                        // word "Prince"
```

