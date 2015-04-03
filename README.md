## About

<img src="https://raw.github.com/yogthos/instant-pdf/master/logo.png"
title="Instant PDF" align="left" padding="5px" width="100" height="50"/>
The service accepts POST requests with JSON body and returns PDF documents,
the document generation is done by the [clj-pdf](https://github.com/yogthos/clj-pdf) library.
You can see it in action [here](http://instantpdf.herokuapp.com/).

<br clear=all /><br />

## Usage

You will need [Leiningen](http://leiningen.org/) to build the service.

To run standalone:

```bash
lein ring uberjar
java -jar target/instant-pdf-0.2.3-standalone.jar <optional port>
```

To package as a WAR for app server deployment:
```bash
lein ring uberwar
```

Once the service is running you will be able to make a POST request to it and pass in a
JSON or a Markdown document as the request. The JSON documents must be submitted using the form parameter called `json-input`.

## Syntax

The document must be an array containing a map representing the
metadata as the first element, followed by one or more elements, eg:

```javascript
[{}, "my document"]

[{"title":"My title", "size":"a4"}, ["paragraph", "some text"]]
```

Example POST with cURL:
```bash
curl -i -X POST -d 'json-input=[{}, ["paragraph", "some text"]]' http://localhost:3000/ > doc.pdf
```

## Document Elements

[Anchor](#anchor),
[Chapter](#chapter),
[Chart](#charting),
[Chunk](#chunk),
[Heading](#heading),
[Image](#image),
[Line](#line),
[List](#list),
[Paragraph](#paragraph),
[Phrase](#phrase),
[Section](#section),
[Spacer](#spacer),
[String](#string),
[Subscript](#subscript),
[Superscript](#superscript),
[Table](#table),
[Table Cell](#table-cell)

## Document Format

### Metadata

All fields in the metadata section are optional:

```javascript
{"right-margin":10,
 "subject":"Some subject",
 "creator":"Jane Doe",
 "doc-header":["inspired by", "William Shakespeare"],
 "bottom-margin":25,
 "pages":true,
 "author":"John Doe",
 "header":"Page header text appears on each page",
 "left-margin":10,
 "title":"Test doc",
 "size":"a4",
 "letterhead":["A simple Letter head"],
 "orientation":"landscape",
 "font":{"size":11},
 "footer":
 "Page footer text appears on each page (includes page number)",
 "top-margin":20}

```

available page sizes:

```javascript
"a0"
"a1"
"a10"
"a2"
"a3"
"a4"
"a5"
"a6"
"a7"
"a8"
"a9"
"arch-a"
"arch-b"
"arch-c"
"arch-d"
"arch-e"
"b0"
"b1"
"b10"
"b2"
"b3"
"b4"
"b5"
"b6"
"b7"
"b8"
"b9"
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
 ```

defaults to A4 page size if none provided

orientation defaults to portrait, unless "landscape" is specified

#### Font

A font is defined by a map consisting of the following parameters, all parameters are optional

* "family": has following options: "courier":, "helvetica":, "times-roman":, "symbol":, "zapfdingbats": defaults to "helvetica":
* "size": is a number default is 11
* "style": has following options: "bold":, "italic":, "bold-italic":, "normal":, "strikethru":, "underline": defaults to "normal":
* "color": is a vector of [r g b] defaults to black

example font:

```javascript
{"style":"bold", "size":18, "family":"helvetica", "color":[0, 234, 123]}

```
note: Font styles are additive, for example setting style "italic": on the phrase, and then size 20 on a chunk inside the phrase, will result with the chunk having italic font of size 20. Inner elements can override style set by their parents.


### Document sections

Each document section is represented by a vector starting with a keyword identifying the section followed by an optional map of metadata and the contents of the section.

#### Anchor

tag "anchor":

optional metadata:

* "id": name of the anchor
* "target": an external link or a name of the anchor this anchor points to, if referencing another anchor then prefix target with #
* "style": font
* "leading": number

content:

iText idiosynchorsies:

* when both font style and leading number are specified the content must be a string
* when leading number is specified content can be a chunk or a string
* when only font style is specified content must be a string
* if no font style or leading is specified then content can be a chunk, a phrase, or a string

```javascript
["anchor", {"style":{"size":15}, "leading":20, "target":"http://google.com"}, "google"]

["anchor", {"id":"targetAnchor"}, "some anchor"]

["anchor", {"target":"#targetAnchor"}, "this anchor points to some anchor"]

["anchor", ["phrase", {"style":"bold"}, "some anchor phrase"]]

["anchor", "plain anchor"]

```

#### Chapter

tag "chapter":

optional metadata:

* none

content:

* string
* paragraph

```javascript
["chapter", "First Chapter"]

["chapter", ["paragraph", "Second Chapter"]]

```

#### Chunk

tag "chunk":

optional metadata:

* "sub": boolean sets chunk to subscript
* "super": boolean sets chunk to superscript

font metadata (refer to Font section for details)

* "family":
* "size":
* "style":
* "color":

```javascript
["chunk", {"style":"bold"}, "small chunk of text"]

["chunk", {"super":true}, "5"]

["chunk", {"sub":true}, "2"]

```

#### Heading

tag "heading":

optional metadata:

* "style": custom font for the heading
* "align": specifies alignement of heading possible valuse "left":, "center":, "right":

```javascript
["heading", "Lorem Ipsum"]

["heading", {"style": {"size":15}}, "Lorem Ipsum"]

["heading", {"style": {"size":10, "color":[100, 40, 150], "align":"right"}}, "Foo"]

```

#### Image

tag "image":

image data can be a base64 string, a string representing URL or a path to file,
images larger than the page margins will automatically be scaled to fit.

optional metadata:

* "xscale": number - percentage relative to page size
* "yscale": num - percentage relative to page size
* "width": num - set width for image: overrides scaling
* "height": num - set height for image: overrides scaling
* "align": "left":, "center":, "right":
* "annotation": ["title" "text"]
* "pad-left": number
* "pad-right": number
* "base64": boolean - if set the image is expected to be a Base64 string

```javascript
["image",
 {"xscale":0.5,
  "yscale":0.8,
  "align":"center",
  "annotation":["FOO", "BAR"],
  "pad-left":100,
  "pad-right":50}, ["read", "mandelbrot.jpg"]]

["image", "test/mandelbrot.jpg"]

["image", "http://clojure.org/space/showimage/clojure-icon.gif"]

```

#### Line

tag "line":

optional metadata:

* "dotted": boolean
* "gap": number ;space between dots if line is dotted

creates a horizontal line

```javascript
["line"]

["line", {"dotted":true}]

["line", {"dotted":true, "gap":10}]

```


#### List

tag "list":

optional metadata:

* "numbered":            boolean
* "lettered":            boolean
* "roman":               boolean
* "greek":               boolean
* "dingbats":            boolean
* "dingbats-char-num":   boolean
* "dingbatsnumber":      boolean
* "dingbatsnumber-type": boolean

content:

* strings, phrases, or chunks


```javascript
["list", {"roman":true}, ["chunk", {"style":"bold"}, "a bold item"],
 "another item", "yet another item"]

```

#### Paragraph

tag "paragraph":

optional metadata:

* "indent": number
* "keep-together": boolean
* "leading": number
* "align": "left":, "center":, "right":

font metadata (refer to Font section for details)

* "family":
* "size":
* "style":
* "color":

content:

* one or more elements (string, chunk, phrase, paragraph)

```javascript
["paragraph", "a fine paragraph"]

["paragraph", {"keep-together":true, "indent":20}, "a fine paragraph"]

["paragraph",
 {"style":"bold",
  "size":10,
  "family":"halvetica",
  "color":[0, 255, 221]},
 "Lorem ipsum dolor sit amet, consectetur adipiscing elit."]

"font"

["paragraph", {"indent":50, "color":[0, 255, 221]},
 ["phrase", {"style":"bold", "size":18, "family":"halvetica"},
  "Hello Clojure!"]]

["paragraph", "256", ["chunk", {"super":true}, "5"], " or 128",
 ["chunk", {"sub":true}, "2"]]

```

#### Phrase

tag "phrase":

optional metadata:

* "leading": number

font metadata (refer to Font section for details)

* "family":
* "size":
* "style":
* "color":

content:

* strings and chunks


```javascript
["phrase", "some text here"]

["phrase",
 {"style":"bold",
  "size":18,
  "family":"halvetica",
  "color":[0, 255, 221]}, "Hello Clojure!"]

["phrase", ["chunk", {"style":"italic"}, "chunk one"],
 ["chunk", {"size":20}, "Big text"], "some other text"]

```

#### Section

tag "section":

Chapter has to be the root element for any sections. Subsequently sections can only be parented under chapters and other sections, a section must contain a title followed by the content

optional metadata:

* "indent": number

```javascript
["chapter", ["paragraph", {"color":[250, 0, 0]}, "Chapter"],
 ["section", "Section Title", "Some content"],
 ["section", ["paragraph", {"size":10}, "Section Title"],
  ["paragraph", "Some content"], ["paragraph", "Some more content"],
  ["section", {"color":[100, 200, 50]},
   ["paragraph", "Nested Section Title"],
   ["paragraph", "nested section content"]]]]

```

#### Spacer

tag "spacer":

creates a number of new lines equal to the number passed in (1 space is default)

```javascript
["spacer"]

["spacer", 5]

```

#### String

A string will be automatically converted to a paragraph

```
"this text will be treated as a paragraph"
```

#### Subscript

tag "subscript":

optional metadata:

* "style": font

creates a text chunk in subscript

```javascript
["subscript", "some subscript text"]

["subscript", {"style":"bold"}, "some bold subscript text"]

```

#### Superscript

tag "superscript":

optional metadata:

* "style": font

creates a text chunk in subscript

```javascript
["superscript", "some superscript text"]

["superscript", {"style":"bold"}, "some bold superscript text"]

```

#### Table

tag "table":

metadata:

* "align": table alignment on the page can be: "left":, "center":, "right":
* "color":  `[r g b]` (int values)
* "header": [{"color": [r g b]} "column name" ...] if only a single column name is provided it will span all rows
* "spacing": number
* "padding": number
* "border": boolean
* "border-width": number
* "cell-border": boolean
* "width": number signifying the percentage of the page width that the table will take up
* "widths": vector list of column widths in percentage
* "header": is a vector of strings, which specify the headers for each column, can optionally start with metadata for setting header color
* "offset": number
* "num-cols": number

```javascript
["table",
 {"header":["Row 1", "Row 2", "Row 3"],
  "width":50,
  "border":false,
  "cell-border":false},
 [["cell", {"colspan":2}, "Foo"], "Bar"], ["foo1", "bar1", "baz1"],
 ["foo2", "bar2", "baz2"]]

["table", {"border-width":10, "header":["Row 1", "Row 2", "Row 3"]},
 ["foo", "bar", "baz"], ["foo1", "bar1", "baz1"],
 ["foo2", "bar2", "baz2"]]

["table",
 {"border":false,
  "widths":[2, 1, 1],
  "header":[{"color":[100, 100, 100]}, "Singe Header"]},
 ["foo", "bar", "baz"], ["foo1", "bar1", "baz1"],
 ["foo2", "bar2", "baz2"]]

["table",
 {"cell-border":false,
  "header":[{"color":[100, 100, 100]}, "Row 1", "Row 2", "Row 3"],
  "cellSpacing":20,
  "header-color":[100, 100, 100]},
 ["foo",
  ["cell",
   ["phrase",
    {"style":"italic",
     "size":18,
     "family":"halvetica",
     "color":[200, 55, 221]},
    "Hello Clojure!"]],
  "baz"],
 ["foo1", ["cell", {"color":[100, 10, 200]}, "bar1"], "baz1"],
 ["foo2", "bar2", "baz2"]]

```

#### Table Cell

Cells can be optionally used inside tables to provide specific style for table elements

tag "cell":

metadata:

* "color": `[r g b]` (int values)
* "colspan": number
* "rowspan": number
* "border": boolean
* "set-border": `["top": "bottom": "left": "right]`": list of enabled borders, pass empty vector to disable all borders
* "border-width": number
* "border-width-bottom": number
* "border-width-left": number
* "border-width-right": number
* "border-width-top": number

content:

Cell can contain any elements such as anchor, annotation, chunk, paragraph, or a phrase, which can each have their own style

note: Cells can contain other elements including tables

```javascript
["cell", {"colspan":2}, "Foo"]

["cell", {"colspan":3, "rowspan":2}, "Foo"]

["cell",
 ["phrase",
  {"style":"italic",
   "size":18,
   "family":"halvetica",
   "color":[200, 55, 221]}, "Hello Clojure!"]]

["cell", {"color":[100, 10, 200]}, "bar1"]

["cell",
 ["table",
  ["Inner table Col1", "Inner table Col2", "Inner table Col3"]]]

```

### Charting

tag "chart":

metadata:

* "type":        - bar-chart, line-chart, pie-chart
* "x-label":     - only used for line and bar charts
* "y-label":     - only used for line and bar charts
* "time-series": - only used in line chart
* "time-format": - can optionally be used with time-series to provide custom date formatting, defaults to "yyyy-MM-dd-HH"mm:ss"":
* "horizontal":  - can be used with bar charts and line charts, not supported by time series
* "title":

additional image metadata

* "xscale": number - percentage relative to page size
* "yscale": num - percentage relative to page size
* "width": num - set width for image: overrides scaling
* "height": num - set height for image: overrides scaling
* "align": "left|center|right"
* "annotation": ["title" "text"]
* "pad-left": number
* "pad-right": number

#### bar chart

```javascript
["chart",
 {"type":"bar-chart",
  "title":"Bar Chart",
  "x-label":"Items",
  "y-label":"Quality"}, [2, "Foo"], [4, "Bar"], [10, "Baz"]]

```

#### line chart

if "time-series": is set to true then items on x axis must be dates, the default format is "yyyy-MM-dd-HH"mm:ss"":, for custom formatting options refer [here](http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html)

```javascript
["chart",
 {"type":"line-chart",
  "title":"Line Chart",
  "x-label":"checkpoints",
  "y-label":"units"},
 ["Foo", [1, 10], [2, 13], [3, 120], [4, 455], [5, 300], [6, 600]],
 ["Bar", [1, 13], [2, 33], [3, 320], [4, 155], [5, 200], [6, 300]]]

```

```javascript
["chart",
 {"x-label":"time",
  "y-label":"progress",
  "time-series":true,
  "title":"Time Chart",
  "type":"line-chart"},
 ["Incidents", ["2011-01-03-11:20:11", 200],
  ["2011-02-11-22:25:01", 400], ["2011-04-02-09:35:10", 350],
  ["2011-07-06-12:20:07", 600]]]

```

```javascript
["chart",
 {"type":"line-chart",
  "time-series":true,
  "time-format":"MM/yy",
  "title":"Time Chart",
  "x-label":"time",
  "y-label":"progress"},
 ["Occurances", ["01/11", 200], ["02/12", 400], ["05/12", 350],
  ["11/13", 600]]]

```

#### pie chart

```javascript
["chart", {"type":"pie-chart", "title":"Big Pie"}, ["One", 21],
 ["Two", 23], ["Three", 345]]

```

### Complete example
```javascript
[{"subject":"Some subject",
  "creator":"Jane Doe",
  "doc-header":["inspired by", "William Shakespeare"],
  "author":"John Doe",
  "header":"page header",
  "title":"Test doc",
  "size":"a4",
  "footer":"page"},
 ["heading", "Lorem Ipsum"],
 ["paragraph",
  ["phrase",
   {"color":[0, 255, 221],
    "style":"italic",
    "family":"halvetica",
    "size":18},
   "Some fancy styled text here"],
  "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec non iaculis lectus. Integer vel libero libero. Phasellus metus augue, consequat a viverra vel, fermentum convallis sem. Etiam venenatis laoreet quam, et adipiscing mi lobortis sit amet. Fusce eu velit vitae dolor vulputate imperdiet. Suspendisse dui risus, mollis ut tempor sed, dapibus a leo. Aenean nisi augue, placerat a cursus eu, convallis viverra urna. Nunc iaculis pharetra pretium. Suspendisse sit amet erat nisl, quis lacinia dolor. Integer mollis placerat metus in adipiscing. Fusce tincidunt sapien in quam vehicula tincidunt. Integer id ligula ante, interdum sodales enim. Suspendisse quis erat ut augue porta laoreet."],
 ["paragraph",
  "Sed pellentesque lacus vel sapien facilisis vehicula. Quisque non lectus lacus, at varius nibh. Integer porttitor porttitor gravida. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus accumsan ante tincidunt magna dictum vulputate. Maecenas suscipit commodo leo sed mattis. Morbi dictum molestie justo eu egestas. Praesent lacus est, euismod vitae consequat non, accumsan in justo. Nam rhoncus dapibus nunc vel dignissim."],
 ["paragraph",
  "Nulla id neque ac felis tempor pretium adipiscing ac tortor. Aenean ac metus sapien, at laoreet quam. Vivamus id dui eget neque mattis accumsan. Aliquam aliquam lacinia lorem ut dapibus. Fusce aliquam augue non libero viverra ut porta nisl mollis. Mauris in justo in nibh fermentum dapibus at ut erat. Maecenas vitae fermentum lectus. Nunc dolor nisl, commodo a pellentesque non, tincidunt id dolor. Nulla tellus neque, consectetur in scelerisque vitae, cursus vel urna. Phasellus ullamcorper ultrices nisi ac feugiat."],
 ["table",
  {"header":[{"color":[100, 100, 100]}, "FOO"], "cellSpacing":20},
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
  ["foo2", "bar2",
   ["cell",
    ["table",
     ["Inner table Col1", "Inner table Col2", "Inner table Col3"]]]]],
 ["paragraph",
  "Suspendisse consequat, mauris vel feugiat suscipit, turpis metus semper metus, et vulputate sem nisi a dolor. Duis egestas luctus elit eget dignissim. Vivamus elit elit, blandit id volutpat semper, luctus id eros. Duis scelerisque aliquam lorem, sed venenatis leo molestie ac. Vivamus diam arcu, sodales at molestie nec, pulvinar posuere est. Morbi a velit ante. Nulla odio leo, volutpat vitae eleifend nec, luctus ac risus. In hac habitasse platea dictumst. In posuere ultricies nulla, eu interdum erat rhoncus ac. Vivamus rutrum porta interdum. Nulla pulvinar dui quis velit varius tristique dignissim sem luctus. Aliquam ac velit enim. Sed sed nisi faucibus ipsum congue lacinia. Morbi id mi in lectus vehicula dictum vel sed metus. Sed commodo lorem non nisl vulputate elementum. Fusce nibh dui, auctor a rhoncus eu, rhoncus eu eros."],
 ["paragraph",
  "Nulla pretium ornare nisi at pulvinar. Praesent lorem diam, pulvinar nec scelerisque et, mattis vitae felis. Integer eu justo sem, non molestie nisl. Aenean interdum erat non nulla commodo pretium. Quisque egestas ullamcorper lacus id interdum. Ut scelerisque, odio ac mollis suscipit, libero turpis tempus nulla, placerat pretium tellus purus eu nunc. Donec nec nisi non sem vehicula posuere et eget sem. Aliquam pretium est eget lorem lacinia in commodo nisl laoreet. Curabitur porttitor dignissim eros, nec semper neque tempor non. Duis elit neque, sagittis vestibulum consequat ut, rhoncus sed dui."],
 ["chart",
  {"x-label":"Items",
   "title":"Bar Chart",
   "type":"bar-chart",
   "y-label":"Quality"},
  [2, "Foo"], [4, "Bar"], [10, "Baz"]],
 ["chart", {"title":"Big Pie", "type":"pie-chart"}, ["One", 21],
  ["Two", 23], ["Three", 345]],
 ["chart",
  {"x-label":"checkpoints",
   "title":"Line Chart",
   "type":"line-chart",
   "y-label":"units"},
  ["Foo", [1, 10], [2, 13], [3, 120], [4, 455], [5, 300], [6, 600]],
  ["Bar", [1, 13], [2, 33], [3, 320], [4, 155], [5, 200], [6, 300]]],
 ["chart",
  {"x-label":"time",
   "title":"Time Chart",
   "type":"line-chart",
   "time-series":true,
   "y-label":"progress"},
  ["Incidents", ["2011-01-03-11:20:11", 200],
   ["2011-01-03-11:25:11", 400], ["2011-01-03-11:35:11", 350],
   ["2011-01-03-12:20:11", 600]]]]
```

### License
***
Copyright © 2015 Dmitri Sotnikov

Distributed under the Eclipse Public License, the same as Clojure.


