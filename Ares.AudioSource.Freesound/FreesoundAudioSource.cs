﻿using Ares.AudioSource;
using RestSharp;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using Ares.ModelInfo;
using Ares.Data;
using System.Net;
using RestSharp.Deserializers;
using System.Threading.Tasks;

namespace Ares.AudioSource.Freesound
{
    public class FreesoundAudioSource : IAudioSource
    {
        public const string AUDIO_SOURCE_ID = "freesound";

        private IRestClient m_Client = new RestClient(Settings.Default.FreesoundApiBase);

        #region IAudioSource interface implementation

        public Bitmap Icon { get { return ImageResources.FreesoundAudioSourceIcon.ToBitmap(); } }
        public string Id { get { return AUDIO_SOURCE_ID; } }
        public string Name { get { return StringResources.FreesoundAudioSourceName; } }

        public Task<IEnumerable<ISearchResult>> Search(string query, int pageSize, int pageIndex, IAbsoluteProgressMonitor monitor, out int? totalNumberOfResults)
        {
            IEnumerable<ISearchResult> results = new List<ISearchResult>();

            // Perform the search
            results = new FreesoundApiSearch(this, this.m_Client, monitor)
                        .GetSearchResults(query, pageSize, pageIndex, out totalNumberOfResults);

            var completionSource = new TaskCompletionSource<IEnumerable<ISearchResult>>();
            completionSource.SetResult((ICollection<ISearchResult>)results);
            return completionSource.Task;
        }

        public Task<IEnumerable<ISearchResult>> SearchSimilar(string id, int pageSize, int pageIndex, IAbsoluteProgressMonitor monitor, out int? totalNumberOfResults)
        {
            IEnumerable<ISearchResult> results = new List<ISearchResult>();

            // Perform the search
            results = new FreesoundApiSearch(this, this.m_Client, monitor)
                        .GetSimilarSearchResults(id, pageSize, pageIndex, out totalNumberOfResults);

            var completionSource = new TaskCompletionSource<IEnumerable<ISearchResult>>();
            completionSource.SetResult((ICollection<ISearchResult>)results);
            return completionSource.Task;
        }
        #endregion
    }
}
