from shared.model import EventDetectionProcedure, LiteralParameterBinding


def get_required_literal_parameter(procedure: EventDetectionProcedure, name: str) -> any:
    found = list(filter(lambda b: b.name == name, procedure.parameter_bindings))

    if len(found) == 0:
        raise Exception(f"Required parameter {name} not found!")

    found_binding = found[0]
    if not isinstance(found_binding, LiteralParameterBinding):
        raise Exception(f"Expected parameter {name} to be a literal!")

    return found_binding.literal
