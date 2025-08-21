// global variables to keep track of the request
// and the function to call when done
var ajaxreq=false, ajaxCallbackC;

// ajaxRequest: Sets up a request
function ajaxRequestC(filename) {
   try {
      //make a new request object
      ajaxreq= new XMLHttpRequest();
      ajaxreq.withCredentials = true;
   } catch (error) {
      return false;
   }
   ajaxreq.open('GET', filename);
   ajaxreq.onreadystatechange = ajaxResponseC;
   ajaxreq.send(null);
}

// ajaxResponse: Waits for response and calls a function
function ajaxResponseC() {
   if (ajaxreq.readyState !=4) return;
   if (ajaxreq.status==200) {
      // if the request succeeded...`
      console.log("Status 200")
      if (ajaxCallbackC) {
         ajaxCallbackC();
      }
   } else alert("Request failed: " + ajaxreq.statusText);
   return true;
}