import React, { PropTypes } from 'react';
import { connect } from 'react-redux';

import { fetchTags } from '../redux/modules/tags';


export class Tags extends React.Component {
  static propTypes = {
    // Data
    tags: PropTypes.object.isRequired,
    loading: PropTypes.bool.isRequired,

    // Actions
    fetchTags: PropTypes.func.isRequired,
  }

  componentWillMount() {
    this.props.fetchTags();
  }

  componentWilLReceiveProps(nextProps) {
    // TODO: something like:
    //   if (nextProps.tags !== this.props.tags) { fetch }

    this.props.fetchTags();
  }

  render() {
    return (
      <div>
        <h1>Tags</h1>

        {this._renderTable()}
      </div>
    );
  }

  _renderTable() {
    if (this.props.loading) {
      return <i>Loading...</i>;
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
    // TODO: current page?
    const tagIds = Object.keys(this.props.tags);

    return tagIds.map(id => {
      const tag = this.props.tags[id];

      return (
        <tr key={'tag-' + id}>
          <td>{tag.name}</td>
        </tr>
      );
    });
  }
}

function mapStateToProps(state) {
  return {
    // TODO: 'currently shown' page?
    tags: state.tags.tags,
    loading: state.tags.$loading,
  };
}

export default connect(mapStateToProps, { fetchTags })(Tags);
