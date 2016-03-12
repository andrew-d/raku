import React, { PropTypes } from 'react';
import { Link } from 'react-router';
import { Pagination } from 'react-bootstrap';
import { connect } from 'react-redux';

import { currentTagsSelector, fetchTags } from '../redux/modules/tags';


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


export class Tags extends React.Component {
  static propTypes = {
    // Data
    tags: PropTypes.array.isRequired,
    loading: PropTypes.bool.isRequired,
    maxPages: PropTypes.number.isRequired,

    // Actions
    fetchTags: PropTypes.func.isRequired,
  }

  static contextTypes = {
    router: PropTypes.object.isRequired,
  }

  componentWillMount() {
    // Fetch tags when this component loads.
    this.props.fetchTags(pageFromLocation(this.props));
  }

  componentWillReceiveProps(nextProps) {
    const currPage = pageFromLocation(this.props),
      nextPage = pageFromLocation(nextProps);

    if (nextPage !== currPage) {
      this.props.fetchTags(nextPage);
    }
  }

  render() {
    return (
      <div>
        <h1>Tags</h1>

        <div className='row'>
          <div className='col-xs-12'>
            {this._renderTable()}
          </div>
        </div>

        <div className='row'>
          <div className='col-xs-12 text-center'>
            <Pagination
              prev={true}
              next={true}
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
    if (!this.props.tags || !this.props.tags.length) {
      return <i>No tags</i>;
    }

    return (
      <table className='table table-bordered'>
        <thead>
          <tr>
            <th>Tag</th>
          </tr>
        </thead>

        <tbody>
          {this._renderRows()}
        </tbody>
      </table>
    );
  }

  _renderRows() {
    return this.props.tags.map(tag => {
      return (
        <tr key={'tag-' + tag.id}>
          <td>{tag.name}</td>
        </tr>
      );
    });
  }

  onSelectPage(event, selectedEvent) {
    this.context.router.push({
      pathname: '/tags',
      query: { page: selectedEvent.eventKey },
    });
  }
}

function mapStateToProps(state, ownProps) {
  return {
    tags: currentTagsSelector(state),
    loading: state.tags.$loading,
    maxPages: state.tags.maxPages,
  };
}

export default connect(mapStateToProps, { fetchTags })(Tags);
