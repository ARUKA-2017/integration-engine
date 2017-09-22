require.config({ paths: { 'vs': '/node_modules/monaco-editor/min/vs' }});
require(['vs/editor/editor.main'], function() {
    window.editor = monaco.editor.create(document.getElementById('container'), {
        value: [
        //    '// Create your JSON file here. \n'+
           '{ \n' +
           '}'
        ].join('\n'),
        language: 'json',
        verticalScrollbarSize: 4,
		horizontalScrollbarSize: 4,
    });
});

var spinner = '<svg class="spinner pull-right" width="65px" height="65px" viewBox="0 0 66 66" '+
'xmlns="http://www.w3.org/2000/svg" style="height: 3%;">'+
'<circle class="path" fill="none" stroke-width="6" stroke-linecap="round" cx="33" cy="33"'+
' r="30"></circle> </svg>';

$(document).ready(function(){
        $("#inputFile").change(onChange);
});
    
function onChange(event) {
    var reader = new FileReader();
    reader.onload = onReaderLoad;
    reader.readAsText(event.target.files[0]);
}

function onReaderLoad(event){
    console.log(event.target.result);
    try{
    var obj = JSON.parse(event.target.result);
    window.editor.setValue(event.target.result);
   
   
    } catch(e){
        swal(
            'Invalid JSON',
            'Error Parsing the file',
            'error'
            );

        return false;
    }
    
}

function alert_data(name, family){
    alert('Name : ' + name + ', Family : ' + family);
}

    $(function() {
        
          // We can attach the `fileselect` event to all file inputs on the page
          $(document).on('change', ':file', function() {
            var input = $(this),
                numFiles = input.get(0).files ? input.get(0).files.length : 1,
                label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
            input.trigger('fileselect', [numFiles, label]);
          });
        
          // We can watch for our custom `fileselect` event like this
          $(document).ready( function() {
              $(':file').on('fileselect', function(event, numFiles, label) {
        
                  var input = $(this).parents('.input-group').find('#filename'),
                      log = numFiles > 1 ? numFiles + ' files selected' : label;
        
                  if( input.length ) {
                      input.html(log);
                  } else {
                      if( log ) alert(log);
                  }
        
              });
          });
          
        //   reload();


        });

        
  