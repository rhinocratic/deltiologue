import { useId, useState } from "react";
import { ColorPickerChangeEvent } from "primereact/colorpicker";
import Button from "./elements/Button";
import ColourPicker from "./elements/ColourPicker";
import Label from "./elements/Label";
import TextField from "./elements/TextField";
import Title from "./elements/Title";
import Foot from "./elements/Foot";
import Form from "./elements/Form";
import TextArea from "./elements/TextArea";
import Section from "./elements/Section";
import FileUpload from "./elements/FileUpload";
import Select from "./elements/Select";
import Grid from "./elements/Grid";
import CheckBox from "./elements/CheckBox";
import Explanation from "./elements/Explanation";
import Paragraph from "./elements/Paragraph";
import FieldSet from "./elements/FieldSet";
import CheckBoxField from "./elements/CheckBoxField";
import Legend from "./elements/Legend";
import RadioButton from "./elements/RadioButton";
import RadioButtonGroup from "./elements/RadioButtonGroup";


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

      <Section>

        <Title>Notifications</Title>
        <Paragraph>
          We'll always let you know about important changes, but you pick what else you want to hear about.
        </Paragraph>

        <FieldSet>

          <Legend>By email</Legend>

          <CheckBoxField>
            <CheckBox id="comments" name="comments" />
            <Explanation>
              <Label htmlFor="comments">Comments</Label>
              <Paragraph>Get notified when someones posts a comment on a posting.</Paragraph>
            </Explanation>
          </CheckBoxField>

          <CheckBoxField>
            <CheckBox id="candidates" name="candidates" />
            <Explanation>
              <Label htmlFor="candidates">Candidates</Label>
              <Paragraph>Get notified when a candidate applies for a job.</Paragraph>
            </Explanation>
          </CheckBoxField>

          <CheckBoxField>
            <CheckBox id="offers" name="offers" />
            <Explanation>
              <Label htmlFor="offers">Offers</Label>
              <Paragraph>Get notified when a candidate accepts or rejects an offer.</Paragraph>
            </Explanation>
          </CheckBoxField>

        </FieldSet>

        <FieldSet>
          <Legend>Push Notifications</Legend>
          <Paragraph>These are delivered via SMS to your mobile phone.</Paragraph>

          <RadioButtonGroup>
            <RadioButton id="push-everything" name="push-notifications">
              <Label htmlFor="push-everything">
                Nothing
              </Label>
            </RadioButton>

            <RadioButton id="push-email" name="push-notifications">
              <Label htmlFor="push-email">
                Same as email
              </Label>
            </RadioButton>

            <RadioButton id="push-nothing" name="push-notifications">
              <Label htmlFor="push-nothing">
                No push notifications
              </Label>
            </RadioButton>

          </RadioButtonGroup>

        </FieldSet>

      </Section>

      <Foot>
        <Button secondary type="button">Cancel</Button>
        <Button primary type="submit">Save</Button>
      </Foot>
    </Form>
  );
}