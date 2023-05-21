const search = document.getElementById("search");
const searchContent = document.getElementById("search-content");

// Connect to the WebSocket server
var socket = new SockJS('/websocket');
var stompClient = Stomp.over(socket);

// Generate a unique identifier (UUID)
var uuid = uuidv4();
console.log(uuid);

// Connect to the WebSocket server
stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);

    // Listen for incoming data for the own UUID
    stompClient.subscribe("/topic/" + uuid, data => {
        // Clear previous search results
        searchContent.innerHTML = "";

        // Parse the JSON data
        let array = JSON.parse(data.body);

        if (array.length == 0){
            let h3 = document.createElement("h3");
            h3.textContent = "No Movies Available";
            h3.classList.add("movie-name");
            searchContent.appendChild(h3);
        }else{
            array.forEach((item) => {
                console.log(item);
                let h3 = document.createElement("h3");
                h3.textContent = item;
                h3.classList.add("movie-name");
                searchContent.appendChild(h3);
            });
        }


        // Iterate over the array and create <h3> elements for each movie name

    });
});

// Handle input event on the search input field
search.oninput = () => {
    // Send a message with the movie name and UUID to the server
    console.log(search.value.trim());
    if (search.value.trim() === "") {
        console.log("All empty");
        search.value = "";
        stompClient.send("/app/search/" + uuid + "/" + " ", {}, "");
    } else {
        stompClient.send("/app/search/" + uuid + "/" + search.value.trim(), {}, "");
    }
};
