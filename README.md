<div align="center">
    <img src="https://github.com/SeoulSKY/ChessAI/blob/master/assets/icon.png" width=300 aspect-ratio=1 alt="ChessAI">
    <h1>Chess AI</h1>
</div>

<blockquote align="center">
    Experience the ultimate in strategic gaming with our customizable chess AI
</blockquote>

## Feature Overview

### Play

![play](https://github.com/SeoulSKY/ChessAI/blob/master/assets/play.gif)

### Customize

![customize](https://github.com/SeoulSKY/ChessAI/blob/master/assets/customize.gif)

## How to run

This application requires [Docker](https://www.docker.com/get-started) Please install it first.

To run the app, use the following command:

```
docker compose up
```

When `web-server` prints `Accepting connections at http://localhost:8080` and `ai-server` prints `AI Server is up and running. Ready to receive requests`, it is ready to be used. Then, visit [here](http://localhost:8080) to play chess against AI. Using `Chrome Browser` is recommended.

## Architecture

![Screenshot from 2022-06-27 19-22-16](https://user-images.githubusercontent.com/48105703/176066683-840572dc-ef22-4530-a42b-419d891c560d.png)

## AI Server API
Visit [here](https://seoulsky.github.io/chess-ai/) to read the document.
