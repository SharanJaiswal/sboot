package com.example.exampleApps.cac.Controller;

import com.example.exampleApps.cac.DTO.PaymentRequest;
import com.example.exampleApps.cac.DTO.PaymentResponse;
import com.example.exampleApps.cac.Service.PaymentService;
import com.example.exampleApps.cac.customEditor.FirstNamePropertyEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

//@Controller // responsible for handling incoming HTTP requests. This will require @ResponseBody annotation tag over every controller method. Not required when @RestController is used.
@RestController // Composed of @Controller and @ResponseBody etc.
@RequestMapping("/payments")    // path|value=".../" ,  method=RequestMethod.GET|PUT|POST|DELETE|PATCH|HEAD|OPTIONS|TRACE   -- This can be over methods also but not when @GetMapping(path|value=".../") or any other request method tag is used.
// @RequestMapping annotation is composed of @Reflective and @Mapping, etc., where @Mapping is used to map the RequestMapping.METHODTYPE provided as second param when over class.
//@GetMapping("name")   // these method type annotations cannot be added over classes. They are specifically added over controller methods only.
public class PaymentController {

    @Autowired  // Field injection is not suggested. Instead, use class constructor to assign the instance bean to class private final variables
    PaymentService paymentService;

    // This method will get invoked at every invocation of every controller method and perform custom logic.
//    @InitBinder
//    protected void initBinder(DataBinder binder) {
//        binder.registerCustomEditor(String.class, "firstName", new FirstNamePropertyEditor());   // we passed, the return type of the req-param var type, variable name, custom logic over the incoming param.
//        binder.registerCustomEditor(Date.class, "doj", new DatePropertyEditor());   // We've added 2nd editor. THis could be from totally different controller method.
//
//        binder.setDisallowedFields("mid-name", "maiden-name");    // case-sensitive - blocks other params even if they are present in the req URL as req param.
//        binder.setAllowedFields("firstName", "LAStnaMe");    // case-insensitive - allows only these fields set, blocks rest all. Lower precedence than DisallowedFields.
    // Also, the above field name in all methods could also be the name of the nested variables of DTOs, ie, attributes name at any nested level can be mentioned here as a single name.
//    }

//    @ResponseBody   // Can be used over classes also. If @Controller is used, then this should be present over every controller resource endpoint method. Not required if @RestController is used, as it is part of it. Tells that return type of method should be serialized to only HTTP response type. If it is not included, then spring will try to render the response type as name/value for "view" and try to resolve and render that view as "response_value.jsp". e.g, if return type is "Hello" (String) then spring will look for "Hello.jsp" file.
    @GetMapping("/{id}")    // URL path variable can be anywhere in the URL path.
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long id // This also works with the initBinder method. Extracts values from URL path for controller param binding. Case-sensitive.
//    , @RequestParam ("paramName") String firstName  // ([name=]"...") -- For multiple params multiple @RequestParams are used as method parameters. "name" should match exactly with the params in the URL mentioned after "?" and as key-value pair(s) separated by "&". Default type rendered as String. We can also mention primitive types. For Enums, we need to manually bind them with values. For custom object types, we use Property Editor where we define custom logics for type casting, operations, mappings, etc. before assigning it to the req param variable. For this, we can use @InitBinder tag over a method.

//    , @RequestBody CustomDTOClass customDTOClassObject    // Bind HTTP request body typically JSON to controller method param
    ) {

        // map incoming data to request DTO
        PaymentRequest internalRequestObj = new PaymentRequest();
        internalRequestObj.setPaymentId(id);
        
        // pass this internalRequestObj to further layer of processing
        PaymentResponse payment = paymentService.getPaymentDetailsById(internalRequestObj);

        // Optional making custom values in header
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("My-Header1", "SomeValue1");
        httpHeaders.add("My-Header2", "SomeValue2");

        // return the response DTO
//        return ResponseEntity.ok(payment);
        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(payment);  // .header()* ... .andManyOtherResponseEntities().body() {body should be at last - Builder Design Pattern}
        // ResponseEntity represents the entire HTTP response including headers, status, actual response, etc. It is return type of every method of controller class as ResponseEntity<ActualResponseDTO> .
        // When we use @RestController, springboot internally creates ResponseEntity<ActualResponseDTO> object as return type of controller method whose return type is simple ActualResponseDTO w/o ResponseEntity wrapping. So no need for this when using @RestController. But in case of using @Controller we must use it, otherwise spring will start to look for the "valueofReturnedObject.jsp" view file. For this reason we must use @ResponseBody if we are not using ResponseEntity<> while using @Controller tag.

        // In cases where we are not intended to send the body (DELETE method), we can send ResponseEntity<Void> ResponseEntity.status(HttpStatus.OK)......build() which internally sends body((Object) null).
    }
    /**
     * HTTP response codes: 1xx(informational=b/n the connection=intermediate response status); 2xx(success); 3xx(redirection); 4xx(request validation error); 5xx(server error)
     * 200(OK=get post idempotent calls); 201(CREATED=post=new rsrc is created); 202(ACCEPTED=post=req successful but processing yet to be done); 204(No Content=delete); 206(Partial Content=Post=partial successful=eg, 95/100 changes successful)
     */

    // 301(moved permanently=redirect to new req path=allow new method to be different); 308(Permanent Redirect=like 301 but no change in http method call); 304(Not Modified=get=:::
    // Client made get call. Got response along with "Last-Modified" time in response header. Client cache response. Client made another Get call with "If-Modified-Since" header in request with same as the first call response header value. If time matches in server, server sends 304, cache data in client is used, otherwise new call will execute completely.)
    @GetMapping(path = "/old-get-user")
    public ResponseEntity<Void> getUser() {
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header("Location", "/payments/new-get-user").build();
    }   // 301 status read by client and hence client look for "Location" and its value in response header, to make again call to the new location.
    @GetMapping(path = "/new-get-user")
    public ResponseEntity<String> getNewUser() {
        return ResponseEntity.status(HttpStatus.OK).body("NEW User success");
    }

    /**
     * 4xx(client need to pass correct req to server=business logic fails); 400(Bad Request=get post patch delete=not details not received); 401(Unauthorized=get post patch delete=resource authentication fails); 403(Forbidden=get post patch delete=only admin accessed but client not allowed); 404(not found=get patch delete); 405(Method Not Allowed=get post patch delete=dispatcher servlet might throw this error as control not reach to the controller); 422(Un-processable Entity=get post patch delete=app bussiness logic failure=eg, country is not supported); 429(Too Many Requests= get post patch delete=eg, rate limiting per user); 409(Conflict=patch delete post=eg. prev req is already in progress hence we put lock on it to accept other, until current completes.)
     */

    /**
     * 5xx(something wrong at server); 500(Internal Server error=get post patch delete=generic err code); 501(Not Implemented=get post patch delete); 502(Bad Gateway=get post patch delete=error in proxy)
     */

    /**
     * 1xx(Information=Interim response to communicate request progress or its status before processing the final request); 100(Continue=post=::::
     * Before sending the request, client checks with server if it can handle the req and ready. First client adds few things in header(- content length:1048576 - content type: multipart/form-data - Expect: 100-continue). Server checks in this first req header "-Expect:100-continue" means client is just checking. So server validate everything(authentication, authorization, content-type, length, etc.). If server is okay, return 100 CONTINUE. Client receives 100, and invokes API again w/o -Expect:100-continue in header this time.
     * )
     */
}