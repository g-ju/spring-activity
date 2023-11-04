package main.activity;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
class ActivityModelAssembler implements RepresentationModelAssembler<Activity, EntityModel<Activity>>
{

    @Override
    public EntityModel<Activity> toModel(Activity activity)
    {
        return EntityModel.of(activity,
                              linkTo(methodOn(ActivityController.class).one(activity.getId())).withSelfRel(),
                              linkTo(methodOn(ActivityController.class).all()).withRel("activities"));
    }
}
