import { FunctionComponent, useState } from "react";
import { ColorPickerChangeEvent } from "primereact/colorpicker";
import Button from "./elements/Button";
import ColourPicker from "./elements/ColourPicker";
import Label from "./elements/Label";
import TextField from "./elements/TextField";
import Title from "../text/Title";
import Foot from "./elements/Foot";
import Form from "./elements/Form";
import Section from "./elements/Section";
import FormFields from "./elements/FormFields";

type TagCategoryFormProps = {
  name?: string;
  colour?: string;
}

const TagCategoryForm: FunctionComponent<TagCategoryFormProps> = ({ name, colour }) => {

  const [categoryName, setCategoryName] = useState(name);
  const [categoryColour, setCategoryColour] = useState(colour);

  const handleSetName = (event: React.ChangeEvent<HTMLInputElement>) => {
    setCategoryName(event.target.value);
  }

  const handleSetColour = (event: ColorPickerChangeEvent) => {
    setCategoryColour(event.value?.toString() || "");
  }

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    // submit
  };

  return (
    <Form onSubmit={handleSubmit}>
      <Section>
        <Title>Create a new tag category</Title>
        <FormFields>

          <div className="sm:col-span-1">
            <Label htmlFor="category-colour">Colour</Label>
            <ColourPicker
              id="category-colour"
              value={categoryColour}
              onChange={handleSetColour}
            />
          </div>

          <div className="sm:col-span-3">
            <Label htmlFor="category-name">Name</Label>
            <TextField
              id="category-name"
              name="category-name"
              placeholder="category name"
              value={categoryName}
              onChange={handleSetName}
            />
          </div>

        </FormFields>
      </Section>

      <Foot>
        <Button secondary type="button">Cancel</Button>
        <Button primary type="submit">Save</Button>
      </Foot>
    </Form>
  );
}

export default TagCategoryForm;