# async-request-reply

A demo of the asynchronous request-reply pattern in Java with Spring.

[![](https://mermaid.ink/img/eyJjb2RlIjoic2VxdWVuY2VEaWFncmFtXG4gICAgcGFydGljaXBhbnQgY2xpZW50XG4gICAgcGFydGljaXBhbnQgYXBpIGFzIEFQSVxuXG4gICAgY2xpZW50LT4-YXBpOiBQT1NUIC93b3JrXG4gICAgYXBpLS0-PmNsaWVudDogSFRUUCAyMDJcblxuICAgIGxvb3Agd2hpbGUgd29yayBpcyBub3QgY29tcGxldGVkXG4gICAgICAgIGNsaWVudC0-PmFwaTogR0VUIC93b3JrL3tpZH0vc3RhdHVzXG4gICAgICAgIGFwaS0tPj5jbGllbnQ6IEhUVFAgMjAyIChwZW5kaW5nKVxuXG4gICAgICAgIE5vdGUgb3ZlciBjbGllbnQ6IHJldHJ5IGFmdGVyIHNvbWUgc2Vjb25kc1xuICAgIGVuZFxuXG4gICAgY2xpZW50LT4-YXBpOiBHRVQgL3dvcmsve2lkfS9zdGF0dXNcbiAgICBhcGktLT4-Y2xpZW50OiBIVFRQIDMwMlxuXG4gICAgY2xpZW50LT4-YXBpOiBHRVQgL3dvcmsve2lkfVxuICAgIGFwaS0tPj5jbGllbnQ6IEhUVFAgMjAwIChzdWNjZWVkZWQgb3IgZmFpbGVkKSIsIm1lcm1haWQiOnsidGhlbWUiOiJkZWZhdWx0In0sInVwZGF0ZUVkaXRvciI6ZmFsc2V9)](https://mermaid-js.github.io/docs/mermaid-live-editor-beta/#/edit/eyJjb2RlIjoic2VxdWVuY2VEaWFncmFtXG4gICAgcGFydGljaXBhbnQgY2xpZW50XG4gICAgcGFydGljaXBhbnQgYXBpIGFzIEFQSVxuXG4gICAgY2xpZW50LT4-YXBpOiBQT1NUIC93b3JrXG4gICAgYXBpLS0-PmNsaWVudDogSFRUUCAyMDJcblxuICAgIGxvb3Agd2hpbGUgd29yayBpcyBub3QgY29tcGxldGVkXG4gICAgICAgIGNsaWVudC0-PmFwaTogR0VUIC93b3JrL3tpZH0vc3RhdHVzXG4gICAgICAgIGFwaS0tPj5jbGllbnQ6IEhUVFAgMjAyIChwZW5kaW5nKVxuXG4gICAgICAgIE5vdGUgb3ZlciBjbGllbnQ6IHJldHJ5IGFmdGVyIHNvbWUgc2Vjb25kc1xuICAgIGVuZFxuXG4gICAgY2xpZW50LT4-YXBpOiBHRVQgL3dvcmsve2lkfS9zdGF0dXNcbiAgICBhcGktLT4-Y2xpZW50OiBIVFRQIDMwMlxuXG4gICAgY2xpZW50LT4-YXBpOiBHRVQgL3dvcmsve2lkfVxuICAgIGFwaS0tPj5jbGllbnQ6IEhUVFAgMjAwIChzdWNjZWVkZWQgb3IgZmFpbGVkKSIsIm1lcm1haWQiOnsidGhlbWUiOiJkZWZhdWx0In0sInVwZGF0ZUVkaXRvciI6ZmFsc2V9)

## API

### Create new work [`POST /work`]

#### Request (application/json)

Body params:

* `message`: mock of a work payload (String, mandatory)
* `delay`: time in seconds to complete de work (long, optional, default: 30 seconds)
* `shouldFail`: whether the work should succeed or fail (boolean, optional, default: false)

```json
{
    "message": "This is a message test",
    "delay": 10,
    "shouldFail": false
}
```
    

#### Response `202 Accepted` (application/json)

A new work is always assigned an `UUID` and is created in the `pending` status.

```json
{
    "id": "81a777df-07a1-4506-965f-007697c5a0d7",
    "message": "This is a message test",
    "status": "pending"
}
```

### Get work status [`GET /work/{id}/status`]

Path variables:

* `id`: work id (UUID).

#### Response `202 Accepted`

The work has not completed yet (it is still `pending`).

Headers:

* `Retry-After`: the client should retry after the specified interval in seconds.

#### Response `302 Moved`

The work has completed (i.e., it is either `succeeded` or `failed`).

Headers:

* `Location`: the completed work can be found at this location.

### Get work [`GET /work/{id}`]

Path variables:

* `id`: work id (UUID).

#### Response `404 Not Found`

The work does not exist or has not completed yet.

#### Response `200 OK`

The work has completed (i.e., it is either `succeeded` or `failed`)

```json
{
    "id": "81a777df-07a1-4506-965f-007697c5a0d7",
    "message": "This is a message test",
    "status": "succeeded"
}
```