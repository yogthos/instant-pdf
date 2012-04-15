A service which takes formatted JSON and serves a PDF file. The application is a WAR which can be deployed to any standard app server like Glassfish. 
to run make sure leiningen is installed

lein deps
lein ring uberwar

## Supported Syntax

### Metadata

All fields in the metadata section are optional:

```
{"right-margin":10,
 "subject":"Some subject",
 "creator":"Jane Doe",
 "bottom-margin":25,
 "author":"John Doe",
 "header":["inspired by", "William Shakespeare"],
 "left-margin":10,
 "title":"Test doc",
 "top-margin":20
 "size":"a4"
 "orientation":"landscape"}
```

available page sizes:

    "a0"                  
    "a1"               
    "a2"               
    "a3"               
    "a4"               
    "a5"               
    "a6"               
    "a7"               
    "a8"               
    "a9"               
    "a10"              
    "arch-a"           
    "arch-b"           
    "arch-c"           
    "arch-d"           
    "arch-e"           
    "b0"               
    "b1"               
    "b2"               
    "b3"               
    "b4"               
    "b5"                   
    "b6"                   
    "b7"                   
    "b8"                   
    "b9"                   
    "b10"                  
    "crown-octavo"         
    "crown-quarto"         
    "demy-octavo"          
    "demy-quarto"          
    "executive"            
    "flsa"                 
    "flse"                 
    "halfletter"           
    "id-1"                 
    "id-2"                 
    "id-3"                 
    "large-crown-octavo"   
    "large-crown-quarto"   
    "ledger"                  
    "legal"                   
    "letter"                  
    "note"                    
    "penguin-large-paperback" 
    "penguin-small-paperback" 
    "postcard"                
    "royal-octavo"            
    "royal-quarto"            
    "small-paperback"         
    "tabloid"
    
defaults to A4 page size if none provided

orientation defaults to portrait, unless "landscape" is specified

#### Font

A font is defined by a map consisting of the following parameters, all parameters are optional

* family has following options: "courier", "helvetica", "times-roman", "symbol", "zapfdingbats" defaults to "helvetica"
* size is a number default is 11
* style has following options: "bold", "italic", "bold-italic", "normal", "strikethru", "underline" defaults to "normal"
* color is a vector of [r g b] defaults to black

example font:

    {"style":"bold", "size":18, "family":"helvetica", "color":[0, 234, 123]}

### Document sections

Each document section is represented by a vector starting with a keyword identifying the section followed by an optional map of metadata and the contents of the section.


#### Anchor

tag "anchor"

optional metadata: 

* style font
* leading number

content:
    
iText idiosynchorsies:

* when both font style and leading number are specified the content must be a string
* when leading number is specified content can be a chunk or a string 
* when only font style is specified content must be a string
* if no font style or leading is specified then content can be a chunk, a phrase, or a string

```
   ["anchor", {"style":{"size":15}, "leading":20}, "some anchor"],
   ["anchor", ["phrase", {"style":"bold"}, "some anchor phrase"]],
   ["anchor", "plain anchor"]
```

#### Chunk 

tag chunk

optional metadata: 

* style font

```
   ["chunk", {"style":"bold"}, "small chunk of text"]
```

#### Phrase

tag phrase

optional metadata: 

* style font

content:

* strings and chunks


```
     ["phrase", "some text here"],
     ["phrase",
      {"style":"bold",
       "size":18,
       "family":"halvetica",
       "color":[0, 255, 221]},
      "Hello Clojure!"],
    ["phrase", ["chunk", {"style":"italic"}, "chunk one"],
    ["chunk", {"size":20}, "Big text"], "some other text"]```

#### Paragraph

tag paragraph

optional metadata: 

* indent number
* keep-together boolean

content:

* string
* phrase

```
    ["paragraph", "a fine paragraph"],
    ["paragraph", {"keep-together":true, "indent":20},
     "a fine paragraph"],
    ["paragraph", {"indent":50},
     ["phrase",
      {"style":"bold",
       "size":18,
       "family":"halvetica",
       "color":[0, 255, 221]}, 
      "Hello Clojure!"]]
```

#### Chapter

tag chapter

optional metadata:

* none

content:

* string
* paragraph

```
    ["chapter", "First Chapter"],
    ["chapter", ["paragraph", "Second Chapter"]]
```

#### List

tag list

optional metadata:

* numbered boolean
* lettered boolean
* roman    boolean

content:

* strings, phrases, or chunks


```
    ["list", {"roman":true}, ["chunk", {"style":"bold"}, "a bold item"], "another item", "yet another item"]
```

### A complete example

    [{"right-margin":50,
      "subject":"Some subject",
      "creator":"Jane Doe",
      "bottom-margin":25,
      "author":"John Doe",
      "header":["inspired by", "William Shakespeare"],
      "left-margin":10,
      "title":"Test doc",
      "top-margin":20},
     
     ["chapter", "First Chapter"],
    
     ["anchor", {"style":{"size":15}, "leading":20}, "some anchor"],
     ["anchor", ["phrase", {"style":"bold"}, "some anchor phrase"]],
     ["anchor", "plain anchor"],
     ["chunk", {"style":"bold"}, "small chunk of text"],
     ["phrase", "some text here"],
     ["phrase",
      {"style":"italic",
       "size":18,
       "family":"halvetica",
       "color":[0, 255, 221]},
      "Hello Clojure!"],
     
     ["phrase", ["chunk", {"style":"strikethru"}, "chunk one"],
      ["chunk", {"size":20}, "Big text"], "some other text"],
     
     ["paragraph",
      "is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."],
     ["paragraph", {"indent":50},
      ["phrase",
       {"style":"bold",
        "size":18,
        "family":"halvetica",
        "color":[0, 255, 221]},
       "Hello Clojure!"]],
     ["chapter", ["paragraph", "Second Chapter"]],
     ["paragraph", {"keep-together":true, "indent":20},
      "a fine paragraph"],
     ["list", {"roman":true}, ["chunk", {"style":"bold"}, "a bold item"],
      "another item", "yet another item"]]

 



