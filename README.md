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

mvn jetty:run -Pproduction

## Open project in Browser

http://localhost:8080/myapp

## List of demos

### Login

- Store intended route to Session and use later to route to right view after login

### InitListener

- Use global before enter event for login purposes
- Setup custom error handler
- Give notification if app is open on another tab

### MainLayout

- Configure context root to be "myapp"
- AppLayout with key shortcuts
- Get browser window width
- Find host address
- PageConfigurator example
- Detect Browser window closing by listening "beforeUnload" with JavaScript and @ClientCallable callback
- Setup CustomizedSystemMessages

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
- Access main layout menu
- BeforeLeaveObserver

### GridView

- Custom select column for single select
- Grid context menu
- ContextMenu to jump to years
- ContextMenu to filter by month
- Dynamic cell styling with TemplateRenderer
- Cell styling with setClassNameGenerator
- ComponentRenderer example
- Popup to hide columns
- CheckBox styling in Popup
- Injecting custom property to text field to programmatically change color
- Setup client side event listener with @ClientCallable callback
- Using single RadioButton element from the RadioButtonGroup

### UploadView

- Drag and drop upload example

### SplitLayoutView

- Workaround with Chart resizing, forced re-flow by JavaScript call

### VaadinBoardView

- Chart re-flow workaround by div wrapping
- Charts customization and styling to fit dark theme

### ChartUtil

- Chart drill-down
- Chart plotlines
- Chart tooltip formatter with conditional JavaScript logic 
- Chart zooming / panning

### ThemeVariantsView

- Some theme variant examples

### GridProView

- Workaround issue clicking save button when editor is open

### TreeGridView

- Selected parents subtree highlighting with class name generator
