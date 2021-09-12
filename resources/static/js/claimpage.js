 
    // claimpage.js

    /*

    uses: claiom screen - show hide fields and buttons by type selcted
    changed* uses: history - store claim id to adjust
    uses: plan store plan value on submit buttons 

    initial test: check for existence of field on html if not claim, history
                 exit script. 

    */

    // Shows / Hides fields based on claim type
    // Populates drop down service list based on claim type 
    // communication: hiddenType field   
    //  #typebuttons = division tag where buttons will be placed

    // the event listeners for the combo boxes will be added
    // on claim page load and then unloaded later.

    // needed to use window.onLoad to get fields like
    // id="haveColors" to be defined. 

    window.onload = function() {

        // check if on claim, plan, or history screens.

        var onClaimScreen = document.getElementById("typefields");
        var onPlanScreen = document.getElementById("selectedPlan");
        var onHistoryScreen = document.getElementById("onHistory");
        var onAdmAction = document.getElementById("actionscreen");
        var onUpdatScreen = document.getElementById("UpdateScreen");

        // which screens to style... id = useStyles is the signal.
        // make sure it has the useColor, hcolor, and labelColor fields
        // properly set... 
        var onStyledScreen = document.getElementById("FormStyleClass");

        var okToRun = onClaimScreen || onPlanScreen || onHistoryScreen
                       || onAdmAction
                       || onUpdatScreen;
        if(!okToRun)
        { 
            return;
        }  

 

        if(onStyledScreen)
        {  
             
             /* set left margin for claim register and update screens */
             var claimMarginLeft = "50px";
             var registerMarginLeft = "185px";
             // sold/outline background margins keep screen centered.
             var sb_claimMarginLeft = "15px";
             var sb_registerMarginLeft = "15px";
            debugger;
            //
            var marginLeft = ""; 
            // this applies to the style classes bg-outline, bg-solid etc.
            marginLeft = (onClaimScreen) ? claimMarginLeft : registerMarginLeft;
            // use window.onload to make sure parsing complete for 
            // this to work.
            let root = document.documentElement; 
            //
            var outline = "bg-outline";
            var solid = "bg-solid"; 
            var picture = "bg-image"; 
            var white = "white";
            //
            var divClassElement = document.getElementById("FormStyleClass"); 
            var divClass = divClassElement.className;
            //
            debugger;
            //
            var screenUsesColors = document.getElementById("haveColors").getAttribute("data-color"); 
            if(screenUsesColors == "yes" || divClass === picture) // adjust colors.
            {  
                
                if(divClass == outline || divClass == solid || divClass === picture)
                {   
                    // compute new margin lefts to keep screen centered
                    if(onClaimScreen) 
                         marginLeft = sb_claimMarginLeft; 
                    else 
                         marginLeft = sb_registerMarginLeft;
                    //
                    // sold and outline color when appropriate
                    var userColor= document.getElementById("userColor").getAttribute("data-color");
                    var headerColor= document.getElementById("headerColor").getAttribute("data-color");
                    var labelColor = document.getElementById("labelColor").getAttribute("data-color");
                    var messageColor = document.getElementById("messageColor").getAttribute("data-color");
                    // secondary button border color:  white unless type solid then label color for visabilyut.
                    // primary button same color.
                    var buttonBorders = (divClass == solid) ? labelColor: white;
                    root.style.setProperty('--user-color', userColor); 
                    root.style.setProperty('--h-color', headerColor);
                    root.style.setProperty('--label-color', labelColor);  
                    root.style.setProperty('--message-color', messageColor); 
                    root.style.setProperty('--margin-left', marginLeft);
                    root.style.setProperty('--bb-color', buttonBorders);
                } 
                
            }
        }

        if(onClaimScreen) {

            // alert("onclaim1");
            medselected = document.getElementById("medselected");
            denselected = document.getElementById("denselected");
            visselected = document.getElementById("visselected");
            drgselected = document.getElementById("drgselected");

            medselected.addEventListener('change', function(event) {
 
                // when form submitted hiddenService will contain the selected service.
                var selectedService = event.target.value;
                // alert(selectedService);
                var hiddenService = document.getElementById("hiddenService");
                hiddenService.value = selectedService; 
            });

            denselected.addEventListener('change', function(event) {
 
                // when form submitted hiddenService will contain the selected service.
                var selectedService = event.target.value;
                //alert(selectedService);
                var hiddenService = document.getElementById("hiddenService");
                hiddenService.value = selectedService; 
            });

            visselected.addEventListener('change', function(event) {
 
                // when form submitted hiddenService will contain the selected service.
                var selectedService = event.target.value;
                //alert(selectedService);
                var hiddenService = document.getElementById("hiddenService");
                hiddenService.value = selectedService; 
            });

            drgselected.addEventListener('change', function(event) {
 
                // when form submitted hiddenService will contain the selected service.
                var selectedService = event.target.value;
                //alert(selectedService);
                var hiddenService = document.getElementById("hiddenService");
                hiddenService.value = selectedService; 
            }); 
        } 

        

        if(onPlanScreen)
        {
            // listener for adjustment button click
            document.addEventListener('click', function(event) {

                // check for adjustment submit button
                var elementId = event.target.id;
                if(elementId != "plan") return; 
                   
                var planName = event.target.value;
                // if this works we can remove the data-action above for browser compat.
                // alert("  plan name:  " + planName );
                // store claim id in the hidden field
                var hiddenElement = document.getElementById("selectedPlan");  
                hiddenElement.value = planName;  
                // submit the form
                var s = document.getElementsByTagName("form"); 
                s[0].submit();  

         }); 
        } 

        if(onHistoryScreen) // Adjust Claim
        {
            // listener for adjustment button click
            document.addEventListener('click', function(event) {

                // check for adjustment submit button
                var elementId = event.target.id;
                if(elementId != "adjust") return;

             
                var action = event.target.value.substring(0,6);
                if(action != "Adjust") return; 
 
                // when form submitted hiddenService will contain the selected service.
                var claimIdToAdjust = event.target.value.substring(6);
                // if this works we can remove the data-action above for browser compat.
                // alert(" adjusting " + claimIdToAdjust);
                // store claim id in the hidden field
                var hiddenElement = document.getElementById("adjustedClaimId");  
                hiddenElement.value = claimIdToAdjust; 
                // set action on the submit button - route to controller logic
                var buttonAction = document.getElementById("buttonAction");
                buttonAction.value = "Adjusting";
                // submit the form
                var s = document.getElementsByTagName("form"); 
                s[0].submit();  

         });

        }

        if(onHistoryScreen) // Pay claim
        {
            // listener for adjustment button click
            document.addEventListener('click', function(event) {

                // check for adjustment submit button
                var elementId = event.target.id;
                if(elementId != "pay") return; 

                 
                var action = event.target.value.substring(0,3);
                if(action != "Pay") return; 
  
                var claimIdToPay = event.target.value.substring(3);
                // if this works we can remove the data-action above for browser compat.
                //alert(" paying " + claimIdToPay);
                // store claim id in the hidden field

                //* popup for payment amount
                var input = prompt("Enter Payment Amount","");
                // now edit it.
                var value = Number.parseFloat(input);
                if(isNaN(value))
                {
                    alert("Sorry, you must enter a valid amount.") 
                    var s = document.getElementsByTagName("form"); 
                    s[0].submit();   
                     
                }
                var amount = parseFloat(input); 
                //
                var payClaimId = document.getElementById("payClaimId");
                var paymentAmount = document.getElementById("paymentAmount");
                // hidden claim id
                payClaimId.value = claimIdToPay;
                // hidden amou9nt
                var stringAmount = amount.toString();
                paymentAmount.value = stringAmount;   
                // set action on the submit button - route to controller logic
                var buttonAction = document.getElementById("buttonAction");
                buttonAction.value = "Paying";
                // submit the form
                var s = document.getElementsByTagName("form"); 
                s[0].submit();   
        });


    }



        if(onAdmAction)
        {
            // listener for adjustment button click
            document.addEventListener('click', function(event) {

                // check for adjustment submit button
                var elementId = event.target.id;
                if(elementId != "action") return; 
                   
                var actionName = event.target.value; 
                // alert("  action Name :  " + actionName );

                // store action in hidden field.
                var hiddenElement = document.getElementById("buttonAction");  
                hiddenElement.value = actionName;  
                // submit the form
                var s = document.getElementsByTagName("form"); 
                s[0].submit();   
            });
            
          } 
            
        if(onClaimScreen) {
 
            blueFields(); // turn fields blue for claim adjustment screen.
            
            loadFunction(); // get type default/current and set buttons and type fields.

        }

    };

    function loadFunction() {


        var hiddenType = "";

        try {
         

         


            // first time: hidden type : empty - default to 'm' = medical.
            // second for edit error: set to hidden type  

             hiddenType = document.getElementById('hiddenType').value;  

            //alert("loading hidden type " + hiddenType);

            if (hiddenType == null || hiddenType == '?' || hiddenType == '') {

                useType = "m"; 
                hiddenType.value = "m";

            } else {

                useType = hiddenType;
            }
 
            dt(useType);

    }
    catch(error)
    { 
            // if null not on claim screen
            if(hiddenType == null)
            {
                return;
            }
    }


    }

    function dMedical() {
        // solve quotes in quotes issue 
        dt("m");
    }

    function dDental() {
        // solve quotes in quotes issue 
        dt("d");
    }


    function dVision() {
        // solve quotes in quotes issue 
        dt("v");
    }


    function dDrug() {
        // solve quotes in quotes issue 
        dt("x");
    }

 

    function dt(newTypeValue) {

        // call with start to set initial button configuration on screen. 
        // the old type is in a hidden field so we can remove the old button
        // the new type is passed from the clicked button so we can add the new button 
        // note: collapse did not work for non-tr elements and button can not be put 
        // in tr. 
        // how this works: remove the old-selected-type and add old-delected-type
        // remove new-deselected and add new selected  
        // initially add 1 med selected and 3 deselected other types.

        var hiddenType = document.getElementById('hiddenType');   
     
        try { 
             

            if (newTypeValue === "m" || newTypeValue === "d"
             || newTypeValue === "v" || newTypeValue === "x") {
                 
            } else {
                alert("ClaimFields script: invalid type to script: " + value);
                return;
            } 

           
            
             var medicalButtonSelected = '<button id="medButton" type="button" class="btn st1  btn-danger">Medical</button>';  
             var medicalButtonDeselected = '<button onclick="dMedical()" id="medButton2" type="button" class="btn st1 btn-outline-danger">Medical</button>'; 
              
             var dentalButtonSelected = '<button id="denButton" type="button" class="btn st1 btn-primary">Dental</button>';
             var dentalButtonDeselected = '<button onclick="dDental()" id="denButton2" type="button" class="btn st1 btn-outline-primary">Dental</button>';


            var visionButtonSelected = '<button id="visButton" type="button" class="btn st1  btn-warning">Vision</button>';
            var visionButtonDeselected = '<button onclick="dVision()" id="visButton2" type="button" class="btn st1  btn-outline-warning">Vision</button>';
             

            var drugButtonSelected = '<button id="drgButton" type="button" class="btn st1 btn-success">Drug</button>';
            var drugButtonDeselected = '<button onclick="dDrug()" id="drgButton2" type="button" class="btn st1  btn-outline-success">Drug</button>';

            var somethingWentWrong = "<p>Something went wrong bad type. </p>"; 

            switch (newTypeValue) {

                case 'm': 

                    $("#typebuttons").empty();
                    $("#typebuttons").append(medicalButtonSelected); 
                    $("#typebuttons").append(dentalButtonDeselected);  
                    $("#typebuttons").append(visionButtonDeselected); 
                    $("#typebuttons").append(drugButtonDeselected); 
                    break;

                case 'd': 

                    $("#typebuttons").empty();
                    $("#typebuttons").append(medicalButtonDeselected);
                    $("#typebuttons").append(dentalButtonSelected);  
                    $("#typebuttons").append(visionButtonDeselected);
                    $("#typebuttons").append(drugButtonDeselected);  
                    break;

                case 'v':

                    $("#typebuttons").empty(); 
                    $("#typebuttons").append(medicalButtonDeselected); 
                    $("#typebuttons").append(dentalButtonDeselected);  
                    $("#typebuttons").append(visionButtonSelected);  
                    $("#typebuttons").append(drugButtonDeselected); 
                    break;

                case 'x':

                    $("#typebuttons").empty(); 
                    $("#typebuttons").append(medicalButtonDeselected); 
                    $("#typebuttons").append(dentalButtonDeselected);  
                    $("#typebuttons").append(visionButtonDeselected);  
                    $("#typebuttons").append(drugButtonSelected); 
                    break; 

                default:

                    $("#typebuttons").empty(); 
                    $("#typebuttons").append(somethingWentWrong);
                    break; 

            }  

            debugger;
             
       

            // different drop downs show by claim type.
           //  SwitchServiceDropDowns(newTypeValue); 

        
            // LOAD CLAIM TYPE INTO HIDDEN FIELD   
            hiddenType.value = newTypeValue;

            // update claim service with default value in the
            // service drop down box. 
            loadFirstServiceValueWhenTypeChanges(newTypeValue); 

            // different claim fields show by claim type.
            SwitchTypefields(newTypeValue);
 

        } catch (err) {

              alert("Claim Fields script error: " + err); 
        }

        function loadFirstServiceValueWhenTypeChanges(type)
        {
            // when user changes claim type put first drop down box value 
            // in the hidden field.  

            medselected = document.getElementById("medselected");
            denselected = document.getElementById("denselected");
            visselected = document.getElementById("visselected");
            drgselected = document.getElementById("drgselected");
            var service = ""; 

            switch(type)
            {
                case 'm' : service = medselected.value; break;
                case 'd' : service = denselected.value; break;
                case 'v' : service = visselected.value; break;
                case 'x' : service = drgselected.value; break;
                default: break;
            }  
            var hiddenService = document.getElementById("hiddenService");
            hiddenService.value = service; 
        }

        function SwitchTypefields(value) { 
           
            var showMed = (value === "m");
            var showDen = (value === "d");
            var showVis = (value === "v");
            var showDrg = (value === "x");

            // show fields for claim tyhpe
            var medtypefields = document.getElementById("am");
            var dentypefields = document.getElementById("dm");
            var vistypefields = document.getElementById("vm");
            var drgtypefields = document.getElementById("xm");

            medtypefields.style.display = showMed ? 'inline' : 'none';
            dentypefields.style.display = showDen ? 'inline' : 'none';
            vistypefields.style.display = showVis ? 'inline' : 'none';
            drgtypefields.style.display = showDrg ? 'inline' : 'none'; 

            // show service lists for type with label included in the div.
            medlistbox = document.getElementById("mdiv");
            denlistbox = document.getElementById("ddiv");
            vislistbox = document.getElementById("vdiv");
            drglistbox = document.getElementById("xdiv");
 
            medlistbox.style.display = showMed ? 'inline' : 'none';
            denlistbox.style.display = showDen ? 'inline' : 'none';
            vislistbox.style.display = showVis ? 'inline' : 'none';
            drglistbox.style.display = showDrg ? 'inline' : 'none';
            
            blueFields(value);
 
            solidLabels(value);
        } 
          
    }  

    
       
    function storeService(service) {

        // storeService()
           
        // user clicks on service list box - 
        // store selection

        //alert("store service: " + service);

        var hiddenElement = document.getElementById("hiddenService");   
        hiddenElement.value = service;

        // controller will read field.
        var s = document.getElementsByTagName("form"); 
        s[0].submit();  
    };
 
 
    function blueFields(value)
    {

        // are we on claim screen
        var onClaimScreen = document.getElementById("typefields");
        if(onClaimScreen == null) // not on claim screen
        {
            return;
        } 

        //* blue adjustment claim fields.
        document.documentElement.style
                .setProperty('--adj-color', 'blue');

        // 
        debugger;
        var adjColor = "blue";
        var confine = document.getElementById("c2");
        var release = document.getElementById("r2");
        var tooth = document.getElementById("t2");
        var eyeware = document.getElementById("e2");
        var drug = document.getElementById("a2");
        switch(value)
        {
            case "m": confine.style.color = adjColor; 
                      release.style.color = adjColor;
                      break;
            case "d": tooth.style.color = adjColor;
                      break;
            case "v": eyeware.style.color = adjColor;
                      break;
            case "x": drug.style.color = adjColor;
                      break;
            default: break;
        }  
    }

    function solidLabels(value) {

        // when solid style is selected for the claim screen
        // the type and drop down service box should be 
        // colored to match the other labels using 'labelColor'
        // for easy readability.

         // are we on claim screen
         var onClaimScreen = document.getElementById("typefields");
         if(onClaimScreen == null) // not on claim screen
         {
             return;
         } 

         var screenUsesColors = document.getElementById("haveColors").getAttribute("data-color"); 
         if(screenUsesColors == "yes") // solid or outline styles applied.
         {  
             let root = document.documentElement; 
             var outline = "bg-outline";
             var solid = "bg-solid"; 
             var divClassElement = document.getElementById("FormStyleClass"); 
             var divClass = divClassElement.className;
             if(divClass != solid)
             { 
                 return;
             }

             // keep going... solid style used , next set  type / service labels to match labelColor.
         }
         else
         {
             // exit we are only interested in solid styles
             return;
         }

         debugger;
         // type labels c1, r1, t1, e1, a1;
         var confine = document.getElementById("c1");
         var release = document.getElementById("r1");
         var tooth = document.getElementById("t1");
         var eyeware = document.getElementById("e1");
         var drug = document.getElementById("a1");
         // service label  
         // mlab, dlab, vlab, xlab;
         var mlab = document.getElementById("mlab");
         var dlab = document.getElementById("dlab");
         var vlab = document.getElementById("vlab");
         var xlab = document.getElementById("xlab");
         //
         switch(value)
         {
             case "m": confine.style.color = labelColor; 
                       release.style.color = labelColor;
                       mlab.style.color = labelColor;
                       break;
             case "d": tooth.style.color = labelColor;
                       dlab.style.color = labelColor;
                       break;
             case "v": eyeware.style.color = labelColor;
                       vlab.style.color = labelColor;
                       break;
             case "x": drug.style.color = labelColor;
                       xlab.style.color = labelColor;
                       break;
             default: break;
         }  
    }

