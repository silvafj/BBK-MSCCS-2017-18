// Filter tournament matches by player
function filterByPlayer(matches) {
  const playerFilters = {
    "equals": player => player.name === playerValue,
    "contains": player => player.name.search(playerValue) > -1,
    "none": player => true,
  };

  var playerValue = $("#player").val();
  var playerCondition = $("#player-condition").val();

  if (playerCondition === "none") {
    playerValue = "";
  }

  if (!playerValue) {
    playerCondition = "none";
  }

  const playerFilter = playerFilters[playerCondition];

  console.log("Filter by player (" + playerCondition + " " + playerValue + "): " + playerFilter);

  return matches.filter(match => match.player.find(playerFilter));
}

// Filter tournament matches by number of sets
function filterBySets(matches) {
  const setsFilters = {
    "equals": player => player.set.length == setsValue,
    "greater-than": player => player.set.length > setsValue,
    "less-than": player => player.set.length < setsValue,
    "none": player => true,
  };

  var setsValue = $("#sets-value").val();
  var setsCondition = $("#sets-condition").val();
  if (!setsValue) {
      setsCondition = "none";
  }

  const setsFilter = setsFilters[setsCondition];

  console.log("Filter by sets (" + setsCondition + " " + setsValue + "): " + setsFilter);

  return matches.filter(match => match.player.find(setsFilter));
}

// Filter tournament matches by round
function filterByRound(matches) {
  const roundFilters = {
    "equals": match => match.round == roundValue,
    "greater-than": match => match.round > roundValue,
    "less-than": match => match.round < roundValue,
    "none": match => true,
  };

  var roundValue = $("#round-value").val();
  var roundCondition = $("#round-condition").val();

  if (!roundValue) {
      roundCondition = "none";
  }

  const roundFilter = roundFilters[roundCondition];

  console.log("Filter by round (" + roundCondition + " " + roundValue + "): " + roundFilter);

  return matches.filter(roundFilter);
}

// Loads the JSON file and generates a table with the results
function parseResultsFromJSON(tournament) {
  tournament += ".json";
  console.log("Requesting JSON from " + tournament);

  // Let's have some fun with jQuery!
  $.getJSON(tournament, function(data) {
    // Apply the filters requested
    matches = data.match;
    matches = filterByPlayer(matches);
    matches = filterBySets(matches);
    matches = filterByRound(matches);

    // Generate the table with the results
    console.log("Generating rows for " + data.name + " " + data.year);

    var table = $("#results-table");
    table.find("tbody").remove();

    matches.forEach(function(match) {
      var tbody = $("<tbody>");

      match.player.forEach(function(player) {
        var row = $("<tr>").addClass(player.outcome);
        row.append($("<td>").html(match.round));
        row.append(
          $("<td>")
            .addClass("name")
            .html(player.name)
        );

        var setsCount = 0;
        player.set.forEach(function(set) {
          setsCount++;
          row.append($("<td>").text(set));
        });

        for (var i = setsCount; i < 5; i++) {
          row.append($("<td>"));
        }
        tbody.append(row);
      });

      table.append(tbody);
    });
  });
}

// Don't submit the form to the server and handle the submit from the client.
function formQueryOnSubmit(e) {
  console.log("Form submit");

  // Get the current form fields values and use them for the filtering.
  parseResultsFromJSON($("#tournament").val());

  // This prevents the form from being submitted to the server.
  e.preventDefault();
}

// After the page loads, attach events for client side processing
window.addEventListener("load", function() {
  console.log("Window onload");

  // Using pure JS with getElementById() just to show how it works!
  // getElementById("form-query") is similar, but less useful than
  // $("#form-query") in jQuery
  var formQuery = document.getElementById("form-query");
  formQuery.addEventListener("submit", formQueryOnSubmit);
  formQuery.reset();

  document
    .getElementById("player-condition")
    .addEventListener("change", function(e) {
      document.getElementById("player").style.display =
        e.target.value === "none" ? "none" : "inline";
    });
});
