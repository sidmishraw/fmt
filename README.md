# fmt

```
Author: Sidharth Mishra <sidmishraw@gmail.com>
Version: 1.0.0
JDK: 11
```

[![Build Status](https://travis-ci.com/sidmishraw/fmt.svg?branch=master)](https://travis-ci.com/sidmishraw/fmt)

## About

`fmt` is a simple fluent-API based string-template utility. It is used for providing parametric string formatting –– somewhat like Python's `str.format()`.

```java
var template = "{kong}{{kong}}!";
var result = fmt.inTemplate(template).put("kong", "King").put("king", "Kong").eval(); // KingKong!
```

In `fmt`, the template parameter is defined as `{template-parameter}`. Moreover, `fmt` makes keeps resolving all the template-parameters recursively till it can no longer resolve any more template parameters.

## Approaches

The first approach would be to get a param-key-value pair out and start resolving the template recursively till all the template parameters have been resolved.
For example:

```
template = "{king}{{one}}{{{kong}}}"

params = {
    "king": "kong",
    "one": "two",
    "kong": "king"
}

step 1: let one be picked (because I'm using a hashmap internally),
template1 = "{king}{two}{{{kong}}}"

step 2: let now king is selected
template2 = "kong{two}{{{kong}}}"

step 3: now kong is selected
template3 = "kong{two}{{king}}" << it should stop here -- for one pass >>

but the final result should be:
"kong{two}king",
```

to achieve the final result, `fmt` retries recursively trying to resolve the new template parameters. If it not able to resolve, it unwinds and returns the result.

## Motivation

I needed an utility that provided parameteric substitutions in Java like in Python. The existing MessageFormatter gives indexed based substitution and it is limiting for my use-case.
