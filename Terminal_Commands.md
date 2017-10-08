# terminal commands

### newUniqueKey()
generates a new unique terminal key. all linked glasses have to be repaired after that.

### removeAll()
removes all widgets

### removeWidget(id)
`glassesTerminal.removeWidget(42);`

removes the widget with the specified id 42

### getWidgetCount()
gets the current widget count

### requestResolutionEvents("user")
`glassesTerminal.requestResolutionEvents("ben_mkiv");`
  * (string) playername or empty for all connected glasses

requests the screen resolution event from the specified user.

### getConnectedPlayers()
gets a list of all connected players

### setRenderResolution(String "user", int width, int height) -- set custom overlay render resolution
`glassesTerminal.setRenderResolution("ben_mkiv", 1280, 720);`
  * (string) playername or empty for all connected glasses
  * (int) width
  * (int) height

sets a custom overlay render resolution for player "ben_mkiv"

