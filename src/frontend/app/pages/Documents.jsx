import React, { PropTypes } from 'react';
import { Link } from 'react-router';
import { Pagination } from 'react-bootstrap';
import { connect } from 'react-redux';

import { currentDocumentsSelector, fetchDocuments } from '../redux/modules/documents';


const pageFromLocation = (props) => {
  const { query } = props.location;
  if (!query) {
    return 1;
  }

  const { page } = query;
  if (!page || isNaN(page)) {
    return 1;
  }

  return +page;
};


export class Documents extends React.Component {
  static propTypes = {
    // Data
    documents: PropTypes.array.isRequired,
    loading: PropTypes.bool.isRequired,
    maxPages: PropTypes.number.isRequired,

    // Actions
    fetchDocuments: PropTypes.func.isRequired,
  }

  static contextTypes = {
    router: PropTypes.object.isRequired,
  }

  // Fetch documents when this component loads.
  componentWillMount() {
    this.props.fetchDocuments(pageFromLocation(this.props));
  }

  // Whenever we get new properties, if the page number has changed, we
  // re-fetch the documents.
  componentWillReceiveProps(nextProps) {
    const currPage = pageFromLocation(this.props),
      nextPage = pageFromLocation(nextProps);

    if (nextPage !== currPage) {
      this.props.fetchDocuments(nextPage);
    }
  }

  render() {
    return (
      <div>
        <h1>Documents</h1>

        <div className='row'>
          <div className='col-xs-12'>
            {this._renderTable()}
          </div>
        </div>

        <div className='row'>
          <div className='col-xs-12 text-center'>
            <Pagination
              prev next
              activePage={pageFromLocation(this.props)}
              items={this.props.maxPages}
              onSelect={::this.onSelectPage}
              />
          </div>
        </div>
      </div>
    );
  }

  _renderTable() {
    if (this.props.loading) {
      return <i>Loading...</i>;
    }
    if (!this.props.documents || !this.props.documents.length) {
      return <i>No documents</i>;
    }

    return (
      <table className='table table-bordered'>
        <thead>
          <tr>
            <th>Document</th>
          </tr>
        </thead>

        <tbody>
          {this._renderRows()}
        </tbody>
      </table>
    );
  }

  _renderRows() {
    return this.props.documents.map(doc => {
      return (
        <tr key={'document-' + doc.id}>
          <td>{doc.name}</td>
        </tr>
      );
    });
  }

  onSelectPage(event, selectedEvent) {
    this.context.router.push({
      pathname: '/documents',
      query: { page: selectedEvent.eventKey },
    });
  }
}

function mapStateToProps(state, ownProps) {
  return {
    documents: currentDocumentsSelector(state.documents),
    loading: state.documents.$loading,
    maxPages: state.documents.maxPages,
  };
}

export default connect(mapStateToProps, { fetchDocuments })(Documents);
