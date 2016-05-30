import React, { PropTypes } from 'react';
import { Link } from 'react-router';
import { Pagination } from 'react-bootstrap';
import { connect } from 'react-redux';

import { fetchDocument } from 'redux/modules/documents';
import isLoading from 'utils/isLoading';


function strictToNumber(s, def=null) {
  if (!s) {
    return def;
  }

  const num = +s;
  if (isNaN(num)) {
    return def;
  }

  return num;
}

export class Document extends React.Component {
  static propTypes = {
    // Data
    document: PropTypes.object,
    loading: PropTypes.bool.isRequired,
    routeParams: PropTypes.shape({
      id: PropTypes.string.isRequired,
    }).isRequired,

    // Actions
    fetchDocument: PropTypes.func.isRequired,
  }

  static contextTypes = {
    router: PropTypes.object.isRequired,
  }

  // Fetch the current document when this component loads.
  componentWillMount() {
    const docId = strictToNumber(this.props.routeParams.id);
    if (docId) {
      this.props.fetchDocument(docId);
    }
  }

  // Whenever we get new properties, if the document ID has changed, we
  // re-fetch it from the server.
  componentWillReceiveProps(nextProps) {
    const currId = strictToNumber(this.props.routeParams.id),
      nextId = strictToNumber(nextProps.routeParams.id);

    if (!nextId || !currId) {
      return;
    }

    if (nextId !== currId) {
      this.props.fetchDocument(nextId);
    }
  }

  render() {
    const docId = strictToNumber(this.props.routeParams.id);
    if (!docId) {
      return <h3>Invalid document id</h3>;
    }

    if (this.props.loading) {
      return <i>Loading...</i>;
    }
    if (!this.props.document) {
      return <i>No such document</i>;
    }

    return (
      <div className='row'>
        <div className='col-sm-9'>
          <h1>Document '{this.props.document.name}'</h1>
        </div>

        <div className='col-sm-3'>
          <h2>Tags</h2>

          {this.renderTags()}
        </div>
      </div>
    );
  }

  renderTags() {
    if (!this.props.document.tags) {
      return null;
    }

    const tags = this.props.document.tags.map((tagid) => {
      // TODO(andrew-d): Need to fetch the tag, since it's not loaded if we
      // navigate here directly.
      const tag = this.props.tags[tagid] ?
        <Link to={'/tags/' + tagid}>
          {this.props.tags[tagid].name}
        </Link> :
        <i>Unknown tag</i>;

      return (
        <li key={'tag-' + tagid}>
          {tag}
        </li>
      );
    });

    return (
      <ul>
        {tags}
      </ul>
    );
  }
}

function mapStateToProps(state, ownProps) {
  const docId = ownProps.routeParams.id;
  const doc = state.documents.documents[docId];

  return {
    document: doc,
    tags: state.tags.tags,
    loading: isLoading(doc),
  };
}

export default connect(mapStateToProps, { fetchDocument })(Document);
