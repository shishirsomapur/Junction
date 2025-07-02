import { useSelector } from 'react-redux';
import DocumentForm from './DocumentForm';
import { useParams } from 'react-router-dom';

function EditDocument() {
  const { id } = useParams();
  const documents = useSelector((state) => state.documents.documents);
  const document = documents.find((doc) => doc.id === parseInt(id)) || {};

  return <DocumentForm mode="edit" initialData={document} />;
}

export default EditDocument;
