# devday-demo-flow

## Vaadin 14+ demo application (DevDays May / 2019)

This is demo application is collection of tips-and-tricks and workarounds.
The source of ideas are questions in Vaadin's support channels (Forum, StackOverflow, 
GitHub issue tracker, commercial support, ...)

This application is not architectural demo about the best progrmming practices.

## Setup

Clone project, import it to your IDE and run it with

## Start server

mvn jetty:run

Production mode

mvn jetty:run-exploded -Pproduction

## Open project in Browser

http://localhost:8080/myapp

## List of demos

### MainLayout

- Configure context root to be "myapp"
- AppLayout with key shortcuts
- Get browser window width
- Find host address

### AbsoluteLayoutView

- Simplified absolute layout example

### AccordionView

- The complete demo 

### DialogView

- Simple dialog example
- Form and binding with Bean
- DatePicker with weekend and holiday styles

### FormLayoutView

- Form and binding with Bean 
- Validators

### GridView

- Grid context menu
- ContextMenu to jump to years
- ContextMenu to filter by month
- Scroll to workaround (JavaScript call)
- Dynamic cell styling with TemplateRenderer
- Cell styling with setClassNameGenerator
- ComponentRenderer example
- Popup to hide columns
- CheckBox styling in Popup
- Injecting custom property to text field to programmatically change color

### UploadView

- Drag and drop upload example

### SplitLayoutView

- Workaround with Chart resizing, forced re-flow by JavaScript call

### VaadinBoardView

- Chart re-flow workaround by div wrapping
- Charts customization and styling to fit dark theme

### ThemeVariantsView

- Some theme variant examples

### Local production build

- Yarn and node loaded by maven, see pom.xml
- vaadin-maven-plugin configured to use those instead of loading yarn from GitHub
- Update correct paths to yarn.cmd in project root
