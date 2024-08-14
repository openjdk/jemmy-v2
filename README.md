# Jemmy Swing UI test automation library

Jemmy is a Java library which provides API to simulate user actions on Swing/AWT UI.

Base concept of the library design is "Operator" class proxies.

A code could look something like this:
```
var window = new JFrameOperator("My application");
new JMenuBarOperator(window).pushMenu("Help/About");
var dialog = new JDialogOperator("About my application");
new JLabelOperator(dialog, "My application is very good!");
new JButtonOperator(dialog, "OK").push();
```

See tutorial for more usage examples.