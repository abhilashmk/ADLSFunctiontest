package com.function;

import com.azure.core.http.rest.PagedIterable;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import com.azure.core.credential.TokenCredential;
import com.azure.storage.file.datalake.*;
import com.azure.storage.common.StorageSharedKeyCredential;
import com.azure.storage.file.datalake.DataLakeDirectoryClient;
import com.azure.storage.file.datalake.DataLakeFileClient;
import com.azure.storage.file.datalake.DataLakeFileSystemClient;
import com.azure.storage.file.datalake.DataLakeServiceClient;
import com.azure.storage.file.datalake.DataLakeServiceClientBuilder;
import com.azure.storage.file.datalake.models.ListPathsOptions;
import com.azure.storage.file.datalake.models.PathAccessControl;
import com.azure.storage.file.datalake.models.PathAccessControlEntry;
import com.azure.storage.file.datalake.models.PathItem;
import com.azure.storage.file.datalake.models.PathPermissions;
import com.azure.storage.file.datalake.models.RolePermissions;


import java.util.Optional;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    /**
     * This function listens at endpoint "/api/HttpExample". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpExample
     * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
     */
    @FunctionName("HttpExample")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET, HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        DataLakeServiceClient dataLakeServiceClient = new DataLakeServiceClientBuilder()
        .endpoint("https://adlsgen232.dfs.core.windows.net")
        .sasToken("")
        .buildClient();
    
            context.getLogger().info("Connected to Storage\n");
            
            //dataLakeServiceClient.createFileSystem("testFileSystem");
    
            DataLakeFileSystemClient dataLakeFileSystemClient = dataLakeServiceClient.getFileSystemClient("test");
    
            context.getLogger().info("connected to container\n");
    
            ListPathsOptions options = new ListPathsOptions();
        options.setPath("UPPERCASE");
         
        //context.getLogger().info(dataLakeFileSystemClient.listPaths());

        PagedIterable<PathItem> pagedIterable = 
        dataLakeFileSystemClient.listPaths(options, null);

    java.util.Iterator<PathItem> iterator = pagedIterable.iterator();
       
    PathItem item = iterator.next();
    context.getLogger().info("Got Item");
    //context.getLogger().info(item);
    while (item != null)
    {
        context.getLogger().info(item.getName());


        if (!iterator.hasNext())
        {
            break;
        }
            
        item = iterator.next();
    }
            
        return request.createResponseBuilder(HttpStatus.OK).body("Done").build();
        
    }
}
