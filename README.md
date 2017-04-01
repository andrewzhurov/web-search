# web-search

When up and ran provides search api.

Searches among forums and blogs by your text phrase.

Powered by Yandex Search service.

## Up and run

clone the repo

`lein run` to launch server instance

## Usage

To get overall statistics of your phrase appearance.
http://your-server.com:8080/search?query=jet&query=smap&plimit=1

`query` - search phrase. `plimit` - limit max number parallel requests

Others api currently unavailable

## Suggested improvements

- show actual links in foldable containers
- make `plimit` optional

## Fix
- phrase search (spaces to pluses)
