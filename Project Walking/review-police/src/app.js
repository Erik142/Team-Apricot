const { Webhooks, createNodeMiddleware } = require("@octokit/webhooks");

const webhooks = new Webhooks({
    secret: process.env.GH_SECRET,
});

webhooks.onAny(({ id, name, payload }) => {
    console.log(id, "Event id")
    console.log(name, "Event name");
    console.log(payload, "Event payload")
});

require("http").createServer(createNodeMiddleware(webhooks)).listen(3000);