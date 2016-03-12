import React, { PropTypes } from 'react';
import { Link } from 'react-router';
import { Pagination } from 'react-bootstrap';
import { connect } from 'react-redux';

import { currentTagsSelector, fetchTags } from '../redux/modules/tags';


export class Tags extends React.Component {
  static propTypes = {
    // Data
    tags: PropTypes.array.isRequired,
    loading: PropTypes.bool.isRequired,
    page: PropTypes.number.isRequired,
    maxPages: PropTypes.number.isRequired,

    // Actions
    fetchTags: PropTypes.func.isRequired,
  }

  componentWillMount() {
    this.props.fetchTags(this.props.page);
  }

  componentWillReceiveProps(nextProps) {
    const { query } = nextProps.location;

    let nextPage = 1;
    if (query.page && query.page > 0) {
      nextPage = query.page;
    }

    if (nextPage !== this.props.page) {
      this.props.fetchTags(nextPage);
    }
  }

  _pageNumber(props) {
    const { query } = props.location;
    if (query.page && query.page > 0) {
        return query.page;
    }

    return 1;
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
              activePage={this.props.page}
              items={this.props.maxPages}
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
}

function mapStateToProps(state, ownProps) {
  return {
    tags: currentTagsSelector(state),
    loading: state.tags.$loading,
    page: state.tags.pageNumber,
    maxPages: state.tags.maxPages,
  };
}

export default connect(mapStateToProps, { fetchTags })(Tags);
