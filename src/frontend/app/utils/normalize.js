import { normalize, Schema, arrayOf } from 'normalizr';


// Main entities
const tagSchema = new Schema('tags');
const documentSchema = new Schema('documents');
const fileSchema = new Schema('files');

// Nesting rules
tagSchema.define({
  documents: arrayOf(documentSchema),
});
documentSchema.define({
  tags: arrayOf(tagSchema),
  files: arrayOf(fileSchema),
});

// Normalizing functions
export function normalizeTag(tag) {
  return normalize(tag, tagSchema);
}
export function normalizeTags(tags) {
  return normalize(tags, arrayOf(tagSchema));
}
export function normalizeDocument(doc) {
  return normalize(doc, documentSchema);
}
export function normalizeDocuments(documents) {
  return normalize(documents, arrayOf(documentSchema));
}
