# Assignment RPS
This assignment implemented `Extras` version including `Lizard` & `Spock` and score

## How to build and run
./mvnw package

docker build -t meltwater/rpc-docker .

docker run -p 4567:4567 -d meltwater/rpc-docker

## How to play

```curl -X POST -H "Content-Type: application/json" localhost:4567/game -d '{"hand": "ROCK"}'```

```curl -X POST -H "Content-Type: application/json" localhost:4567/game -d '{"hand": "PAPER"}'```

```curl -X POST -H "Content-Type: application/json" localhost:4567/game -d '{"hand": "SCISSORS"}'```

```curl -X POST -H "Content-Type: application/json" localhost:4567/game -d '{"hand": "LIZARD"}'```

```curl -X POST -H "Content-Type: application/json" localhost:4567/game -d '{"hand": "SPOCK"}'```

## Response with score

```{"result":"WON","computerHand":"PAPER","score":{"won":1,"draw":0,"lost":0}}```



