var codeArea = new CodeFlask('#codeArea', {
    lineNumbers: true
});

function getCode() {
  return codeArea.getCode();
}