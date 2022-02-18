# dev

Create postgres dbs:

``` shell
createdb todoexample_dev
createdb todoexample_test
```

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
