# dev

- uses postgres with a db named `todoexample_dev`
  - create with `createdb todoexample_dev`

After starting REPL, `(dev)`

## About

This project demonstrates:

- specify endpoints
- writing endpoint tests
- creating a frontend

## migrations

from dev:

``` clojure
(migratus/migrate (db-config))
```
