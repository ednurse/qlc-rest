package biz.sigma7.qlcrest.controller;

import biz.sigma7.qlcrest.controller.representation.FunctionStatusRepresentation;
import biz.sigma7.qlcrest.domain.Function;
import biz.sigma7.qlcrest.repository.FunctionRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/function", produces = MediaType.APPLICATION_JSON_VALUE)
public class FunctionController {

    private final FunctionRepository repository;

    public FunctionController(FunctionRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Function> getAllFunctions() {
        return repository.getAllFunctions();
    }

    @GetMapping("/status/{id}")
    public String getFunctionStatus(@PathVariable("id") int id) {
        return repository.getFunctionStatus(id);
    }

    @PutMapping("/status/{id}/{status}")
    public void setFunctionStatus(@PathVariable("id") int id, @PathVariable("status") int status) {
        repository.setFunctionStatus(id, status);
    }

    @PutMapping("/status")
    public void setFunctionsStatus(@RequestBody List<FunctionStatusRepresentation> statuses) {
        statuses.forEach(status ->
                repository.setFunctionStatus(status.getId(), status.getValue())
        );
    }

}
