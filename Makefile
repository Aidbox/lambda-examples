.EXPORT_ALL_VARIABLES:
.PHONY: test

# include .env
up:
	docker-compose up -d

down:
	docker-compose down

repl:
	clj -A:nrepl -m nrepl.cmdline --middleware "[cider.nrepl/cider-middleware refactor-nrepl.middleware/wrap-refactor]"

test:
	clj -A:nrepl:test
