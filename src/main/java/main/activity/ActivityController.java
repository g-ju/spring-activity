package main.activity;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
class ActivityController
{
    private final ActivityRepository repository;
    private final ActivityModelAssembler assembler;

    ActivityController(ActivityRepository repository, ActivityModelAssembler assembler)
    {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/activities")
    CollectionModel<EntityModel<Activity>> all()
    {
        List<EntityModel<Activity>> activities = repository.findAll()
                                                           .stream()
                                                           .map(assembler::toModel)
                                                           .toList();

        return CollectionModel.of(activities, linkTo(methodOn(ActivityController.class).all()).withSelfRel());
    }

    @GetMapping("/activities/{id}")
    EntityModel<Activity> one(@PathVariable Long id)
    {
        Activity activity = repository.findById(id)
                                      .orElseThrow(() -> new ActivityNotFoundException(id));
        return assembler.toModel(activity);
    }

}
