This service accepts POST requests with JSON markup and returns PDF documents:
```
curl -i -X POST -d 'json-input=[{}, "some text"]' http://yogthos.net/instant-pdf > doc.pdf
```
[see here for source code and syntax](https://github.com/yogthos/instant-pdf)

[see here for an example](example.json)

### License

Distributed under the Eclipse Public License.

***
Copyright (C) 2012 Yogthos
