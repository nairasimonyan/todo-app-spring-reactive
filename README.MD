### Todo list

Simple todo list where you can add item , delete , modify it , mark as done/not done
All Create/Update/Delete actions also trigger events with descriptive explanations
 (e.g. COMPLETED TODO: Read a Good Book)
A reactive streaming endpoint is also implemented using websockets to subscribe to this data
URL:
ws://localhost:9090/stream/events

A sample client subscribed to the events stream is implemented in the application
You can just open http://localhost:9090/index.html using your browser

NOTE: in order to enable the events streaming you have to make the events
 collection in mongo db a capped collection

Please execute following command in Mongo CLI

use todo_item
db.runCommand({ convertToCapped: 'event', size: 8192 })

### Prerequisites

```
JDK 1.8
Maven 3.2+
MongoDB
```


### Running the tests
Included please find integration tests suite covering all API actions


### API 
Headers:
Please send this headers with all API requests


Content-Type: application/json;charset=UTF-8
Accept: application/json;charset=UTF-8


Create ToDo:
Method: POST
http://localhost:9090/todo

{"text": "Read a Book", "isDone": false}

Update ToDo:
Method: PUT
http://localhost:9090/todo/{todo-id}

{"text": "Read a Good Book", "done": false} //updating the name of todo
{"text": "Read a Good Book", "done": true} // Set the todo as completed

Delete a ToDo:
Method: DELETE
http://localhost:9090/todo/{todo-id}

Get All Todo Items:
Method: GET
http://localhost:9090/todo/

Get A ToDo Item By ID:
Method: GET
http://localhost:9090/todo/{todo-id}

