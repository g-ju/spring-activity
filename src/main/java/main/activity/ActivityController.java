package main.activity;

import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @PostMapping("/activities")
    ResponseEntity<EntityModel<Activity>> newActivity(@RequestBody @Valid Activity activity)
    {
        EntityModel<Activity> entityModel = assembler.toModel(repository.save(activity));

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                             .body(entityModel);
    }

    /**
     * Update activity but only if the relevant fields are valid
     */
    @PutMapping("/activities/{id}")
    ResponseEntity<?> updateActivity(@RequestBody Activity newActivity, @PathVariable Long id)
    {
        Optional<Activity> optionalActivity = repository.findById(id);

        if (optionalActivity.isPresent())
        {
            Activity updatedActivity = optionalActivity.get();
            if (newActivity.getName() != null)
            {
                updatedActivity.setName(newActivity.getName());
            }
            if (newActivity.getDescription() != null)
            {
                updatedActivity.setDescription(newActivity.getDescription());
            }

            EntityModel<Activity> entityModel = assembler.toModel(updatedActivity);

            return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                                 .body(entityModel);
        }
        else
        {
            throw new ActivityNotFoundException(id);
        }
    }

    @DeleteMapping("/activities/{id}")
    ResponseEntity<?> deleteActivity(@PathVariable Long id)
    {
        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}
