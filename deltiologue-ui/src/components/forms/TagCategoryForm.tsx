import { useId, useState } from "react";
import { ColorPickerChangeEvent } from "primereact/colorpicker";
import Button from "../form_elements/Button";
import ColourPicker from "../form_elements/ColourPicker";
import Label from "../form_elements/Label";
import TextField from "../form_elements/TextField";
import Title from "../form_elements/Title";
import Foot from "../form_elements/Foot";
import Form from "../form_elements/Form";
import TextArea from "../form_elements/TextArea";
import Section from "../form_elements/Section";
import FileUpload from "../form_elements/FileUpload";
import Select from "../form_elements/Select";
import Grid from "../form_elements/Grid";


export default function TagCategoryForm() {

  const [displayText, setDisplayText] = useState("");
  const [colour, setColour] = useState("");

  const descriptionId = useId();

  const handleOnChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setDisplayText(event.target.value);
  }

  const handleSetColour = (event: ColorPickerChangeEvent) => {
    setColour(event.value?.toString() || "");
  }

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    // submit
  };

  return (
    <Form onSubmit={handleSubmit}>
      <Section>
        <Title>Create a new tag category</Title>
        <Grid>
          <div className="sm:col-span-4">
            <Label htmlFor="description">Category Name</Label>
            <TextField
              name="description"
              id={descriptionId}
              value={displayText}
              placeholder="description"
              onChange={handleOnChange}
            />
          </div>

          <div className="sm:col-span-4">
            <Label htmlFor="colour">Colour</Label>
            <ColourPicker name="colour" value={colour} onChange={handleSetColour} />
          </div>

          <div className="col-span-full">
            <Label htmlFor="about">About</Label>
            <TextArea id="about" name="about" rows={3}></TextArea>
            <p className="mt-3 text-sm/6 text-gray-600">Write a few sentences about yourself.</p>
          </div>

          <div className="col-span-full">
            <Label htmlFor="about">Image Upload</Label>
            <FileUpload />
          </div>
        </Grid>
      </Section>

      <Section>
        <Grid>
          <div className="col-span-full">
            <Label htmlFor="country">Country</Label>
            <Select name="country">
              <option>Germany</option>
              <option>France</option>
              <option>Italy</option>
            </Select>
          </div>

          <div className="sm:col-span-3">
            <label htmlFor="first-name" className="block text-sm/6 font-medium text-gray-900">First name</label>
            <div className="mt-2">
              <input id="first-name" type="text" name="first-name" className="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6" />
            </div>
          </div>

          <div className="sm:col-span-3">
            <label htmlFor="last-name" className="block text-sm/6 font-medium text-gray-900">Last name</label>
            <div className="mt-2">
              <input id="last-name" type="text" name="last-name" className="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6" />
            </div>
          </div>

        </Grid>
      </Section>

      <Foot>
        <Button secondary type="button">Cancel</Button>
        <Button primary type="submit">Save</Button>
      </Foot>
    </Form>
  );
}