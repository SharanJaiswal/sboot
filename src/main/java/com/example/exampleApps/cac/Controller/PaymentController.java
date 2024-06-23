package com.example.exampleApps.cac.Controller;

import com.example.exampleApps.cac.DTO.PaymentRequest;
import com.example.exampleApps.cac.DTO.PaymentResponse;
import com.example.exampleApps.cac.Service.PaymentService;
import com.example.exampleApps.cac.customEditor.FirstNamePropertyEditor;
import org.springframework.beans.factory.annotation.Autowired;
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

//    @ResponseBody   // Can be used over classes also. If @Controller is used, then this should be present over every controller resource endpoint method. Not required if @RestController is used, as it is part of it. Tells that return type of method should be serialized to only HTTP response type. If it is not included, then spring will try to render the response type as name for "view" and try to resolve and render that view as "response_value.jsp". e.g, if return type is "Hello" (String) then spring will look for "Hello.jsp" file.
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

        // return the response DTO
//        return ResponseEntity.ok(payment);
        return ResponseEntity.status(HttpStatus.OK).body(payment);  // .header() ... .andManyOtherResponseEntities()
        // ResponseEntity represents the entire HTTP response including headers, status, actual response, etc. It is return type of every method of controller class as ResponseEntity<ActualResponseDTO> .
        // When we use @RestController, springboot internally creates ResponseEntity<ActualResponseDTO> object as return type of controller method whose return type is simple ActualResponseDTO w/o ResponseEntity wrapping. So no need for this when using @RestController. But in case of using @Controller we must use it, otherwise spring will start to look for the "valueofReturnedObject.jsp" view file. For this reason we must use @ResponseBody along with ResponseEntity while using @Controller tag.
    }
}