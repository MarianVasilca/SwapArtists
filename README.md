# SwapArtists

The goal of the mobile technical challenge is to build a simple application to browse artists.

### Features
* List of artists. It uses a graphql API to load paging data.
* Artist details. It includes the name of the artist, an image, a rating, a list of its releases
* List of favorite artists. Saved locally using Room
* User can add or remove a favorite data from any screen

### Libraries
* [Hilt][hilt] for dependency injection
* [Apollo][apollo] for making GraphQL requests
* [Room][room] for saving data locally
* [Paging][paging] for loading large data sets in chunks
* [Navigation][navigation]
* [Material][material] 
* [Coroutines][coroutines] for executing code asynchronously
* [Coil][coil] for loading images
* [mockito][mockito] for mocking in tests

[hilt]: https://developer.android.com/training/dependency-injection/hilt-android
[apollo]: https://github.com/apollographql/apollo-kotlin
[room]: https://developer.android.com/training/data-storage/room
[paging]: https://developer.android.com/topic/libraries/architecture/paging/v3-overview
[navigation]: https://developer.android.com/guide/navigation
[material]: https://github.com/material-components/material-components-android
[coroutines]: https://developer.android.com/kotlin/coroutines
[coil]: https://github.com/coil-kt/coil
[mockito]: http://site.mockito.org

License
--------
Copyright 2022 Marian Vasilca

Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements.  See the NOTICE file distributed with this work for
additional information regarding copyright ownership.  The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License.  You may obtain a copy of
the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
License for the specific language governing permissions and limitations under
the License.