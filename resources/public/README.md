## Supported Syntax

### Metadata

All fields in the metadata section are optional:

```
{"right-margin":10,
"subject":"Some subject",
"creator":"Jane Doe",
"bottom-margin":25,
"author":"John Doe",
"doc-header":["inspired by", "William Shakespeare"],
"header":"Page header text appears on each page",
"footer":"Page footer text appears on each page (includes page number)",
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
 ["list", {"roman":true}, ["chunk", {"style":"bold"}, "a bold item"], 
                          "another item", 
                          "yet another item"]
```

#### Table

tag table

metadata:

* color  [r g b] int values
* header-color [r g b] int values
* spacing number
* padding number
* header text

```
   ["table", {"header":"A header", "header-color":[100, 100, 100]},
    ["foo", "bar", "baz"], 
    ["foo1", "bar1", "baz1"],
    ["foo2", "bar2", "baz2"]]
```

#### Cell

Cells can be optionally used inside tables to provide specific style for table elements

tag cell

metadata:

* color [r g b] int values

content:

Cell can contain any elements such as anchor, annotation, chunk, paragraph, or a phrase, which can each have their own style

```
   ["cell",
     ["phrase",
      {"color":[200, 55, 221],
       "style":"italic",
       "family":"halvetica",
       "size":18},
     "Hello Clojure!"]]

   ["cell", {"color":[100, 10, 200]}, "bar1"]
```

### Charts

tag chart

metadata:

* type        - bar-chart, line-chart, pie-chart
* x-label     - only used for line and bar charts
* y-label     - only used for line and bar charts
* time-series - only used in line chart
* time-format - can optionally be used with time-series to provide custom date formatting
* horizontal  - can be used with bar charts and line charts, not supported by time series
* title  

#### bar chart
```
["chart",
 {"x-label":"Items",
  "title":"Bar Chart",
  "type":"bar-chart",
  "y-label":"Quality"},
 [2, "Foo"], [4, "Bar"], [10, "Baz"]]
```

#### pie chart
```
["chart", 
 {"title":"Big Pie", 
  "type":"pie-chart"}, 
 ["One", 21], ["Two", 23], ["Three", 345]]]
```

#### line chart
```
["chart",
 {"x-label":"progress",
  "title":"Line Chart",
  "type":"line-chart",
  "y-label":"time"},
 ["Foo", [1, 10], [2, 13], [3, 120], [4, 455], [5, 300], [6, 600]],
 ["Bar", [1, 13], [2, 33], [3, 320], [4, 155], [5, 200], [6, 300]]]
``` 

```
["chart",
 {"x-label":"time",
  "title":"Time Chart",
  "type":"line-chart",
  "time-series":true,
  "y-label":"progress"},
 ["Incidents", ["2011-01-03-11:20:11", 200],
  ["2011-02-11-22:25:01", 400], ["2011-04-02-09:35:10", 350],
  ["2011-07-06-12:20:07", 600]]]
```

```
["chart",
 {"x-label":"time",
  "title":"Time Chart",
  "type":"line-chart",
  "time-series":true,
  "time-format":"MM\/yy",
  "y-label":"progress"},
 ["Occurances", ["01\/11", 200], ["02\/12", 400], ["05\/12", 350],
  ["11\/13", 600]]]
```

### A complete example

```
[{"right-margin":50,
 "subject":"Some subject",
 "creator":"Jane Doe",
 "doc-header":["inspired by", "William Shakespeare"],
 "bottom-margin":25,
 "author":"John Doe",
 "left-margin":10,
 "title":"Test doc",
 "size":"a4",
 "header":"Information accuracy is not guaranteed",
 "footer":"page",
 "top-margin":20},

["paragraph",
 " Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."],
["paragraph", {"indent":50},
 ["phrase",
  {"color":[0, 255, 221],
   "style":"bold",
   "family":"halvetica",
   "size":18},
  "Hello Clojure!"]],
["chapter", ["paragraph", "Second Chapter"]],
["paragraph", {"indent":20, "keep-together":true},
 "a fine paragraph"],
["list", {"roman":true}, ["chunk", {"style":"bold"}, "a bold item"],
 "another item", "yet another item"],
["chapter", "Second Chapter"],
["table",
 {"header":"FOO", "cellSpacing":20, "header-color":[100, 100, 100]},
 ["foo",
  ["cell",
   ["phrase",
    {"color":[200, 55, 221],
     "style":"italic",
     "family":"halvetica",
     "size":18},
    "Hello Clojure!"]],
  "baz"],
 ["foo1", ["cell", {"color":[100, 10, 200]}, "bar1"], "baz1"],
 ["foo2", "bar2", "baz2"]],
["chapter", "Charts"],

["chart",
 {"x-label":"Items",
  "title":"Bar Chart",
  "type":"bar-chart",
  "y-label":"Quality"},
 [2, "Foo"], [4, "Bar"], [10, "Baz"]],

["chart",
 {"x-label":"progress",
  "title":"Line Chart",
  "type":"line-chart",
  "y-label":"time"},
 ["Foo", [1, 10], [2, 13], [3, 120], [4, 455], [5, 300], [6, 600]],
 ["Bar", [1, 13], [2, 33], [3, 320], [4, 155], [5, 200], [6, 300]]],

["chart", {"title":"Big Pie", "type":"pie-chart"}, ["One", 21],
 ["Two", 23], ["Three", 345]]]

```
