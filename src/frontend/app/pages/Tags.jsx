import React, { PropTypes } from 'react';
import { Link } from 'react-router';
import { Pagination } from 'react-bootstrap';
import { connect } from 'react-redux';

import { currentTagsSelector, fetchTags } from '../redux/modules/tags';


export class Tags extends React.Component {
  static propTypes = {
    // Router
    location: PropTypes.object.isRequired,

    // Data
    tags: PropTypes.array.isRequired,
    loading: PropTypes.bool.isRequired,

    // Actions
    fetchTags: PropTypes.func.isRequired,
  }

  componentWillMount() {
    const page = this._pageNumber(this.props);
    this.props.fetchTags(page);
  }

  componentWillReceiveProps(nextProps) {
    const currentPage = this._pageNumber(this.props),
      nextPage = this._pageNumber(nextProps);

    if (nextPage !== currentPage) {
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
              activePage={this._pageNumber(this.props)}
              items={3}
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

function mapStateToProps(state) {
  return {
    tags: currentTagsSelector(state),
    loading: state.tags.$loading,
  };
}

export default connect(mapStateToProps, { fetchTags })(Tags);
